package com.chooloo.www.chooloolib.ui.caller

import android.util.Log
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.domain.model.Contact
import com.chooloo.www.chooloolib.domain.model.ContactDetails
import com.chooloo.www.chooloolib.domain.model.Phone
import com.chooloo.www.chooloolib.domain.model.Recent
import com.chooloo.www.chooloolib.domain.repository.BlockedRepository
import com.chooloo.www.chooloolib.domain.repository.ContactDetailsRepository
import com.chooloo.www.chooloolib.domain.repository.ContactRepository
import com.chooloo.www.chooloolib.domain.repository.PhoneRepository
import com.chooloo.www.chooloolib.domain.repository.RecentRepository
import com.chooloo.www.chooloolib.domain.repository.TelecomRepository
import com.chooloo.www.chooloolib.ui.common.LoadingState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface CallerTarget {
    data class ContactTarget(val contactId: Long) : CallerTarget
    data class RecentTarget(val recentId: Long) : CallerTarget
    data class LinkTarget(val link: String) : CallerTarget
}

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel(assistedFactory = CallerViewModel.Factory::class)
class CallerViewModel @AssistedInject constructor(
    @Assisted target: CallerTarget,
    private val phoneRepository: PhoneRepository,
    private val recentRepository: RecentRepository,
    private val blockedRepository: BlockedRepository,
    private val contactRepository: ContactRepository,
    private val telecomRepository: TelecomRepository,
    private val contactDetailsRepository: ContactDetailsRepository,
) : ViewModel() {
    @AssistedFactory
    interface Factory {
        fun create(target: CallerTarget): CallerViewModel
    }

    data class UiState(
        val loadingState: LoadingState = LoadingState.LOADING,
        val name: String? = null,
        val number: String? = null,
        val contact: Contact? = null,
        val details: Details = Details(),
        val history: List<Recent> = emptyList(),
        val isBlocked: Boolean? = null,
        @StringRes val message: Int? = null,
    ) {
        val hasNumber: Boolean get() = !number.isNullOrEmpty()
    }

    data class Details(
        val phones: List<Phone> = emptyList(),
        val emails: List<String> = emptyList(),
        val organization: String? = null,
        val whatsappNumber: String? = null,
    )

    private data class Caller(val name: String?, val number: String?, val contact: Contact?)

    private sealed interface Load {
        data object Failed : Load
        data class Found(val caller: Caller) : Load
    }

    private val message = MutableStateFlow<Int?>(null)
    private val blockedRefresh = MutableStateFlow(0)

    private val load: Flow<Load> = callerOf(target)
        .map { caller -> caller?.let { Load.Found(it) } ?: Load.Failed }
        .catch { error ->
            Log.e(TAG, "Failed loading caller", error)
            emit(Load.Failed)
        }

    private val caller = load.map { (it as? Load.Found)?.caller }

    private val details: Flow<Details> = caller.map { it?.contact?.id }
        .distinctUntilChanged()
        .flatMapLatest { contactId ->
            if (contactId == null) {
                flowOf(Details())
            } else {
                combine(phoneRepository.phones(contactId), contactDetailsRepository.details(contactId), ::detailsOf)
                    .catch { error ->
                        Log.e(TAG, "Failed loading caller details", error)
                        emit(Details())
                    }
            }
        }

    private val history: Flow<List<Recent>> = combine(caller, details) { caller, details ->
        details.phones.map { it.number }.ifEmpty { listOfNotNull(caller?.number) }.distinct()
    }
        .distinctUntilChanged()
        .flatMapLatest { numbers ->
            recentRepository.recentsOfNumbers(numbers).catch { error ->
                Log.e(TAG, "Failed loading caller history", error)
                emit(emptyList())
            }
        }

    private val isBlocked: Flow<Boolean?> = combine(caller.map { it?.number }.distinctUntilChanged(), blockedRefresh) { number, _ -> number }
        .mapLatest { number ->
            val blockable = number?.takeIf { it.isNotEmpty() && telecomRepository.isDefaultDialer() } ?: return@mapLatest null
            runCatching { blockedRepository.isBlocked(blockable) }
                .onFailure { Log.e(TAG, "Failed reading blocked state", it) }
                .getOrNull()
        }

    val uiState: StateFlow<UiState> = combine(load, details, history, isBlocked, message) { load, details, history, isBlocked, message ->
        val caller = (load as? Load.Found)?.caller
        UiState(
            loadingState = if (caller == null) LoadingState.FAILED else LoadingState.SUCCESS,
            name = caller?.name,
            number = caller?.number,
            contact = caller?.contact,
            details = details,
            history = history,
            isBlocked = isBlocked,
            message = message,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UiState())

    fun onCall(number: String) = launch(R.string.error_call_failed) {
        telecomRepository.callNumber(number)
    }

    fun onDeleteConfirmed() = launch(R.string.error_contact_deletion_failed) {
        uiState.value.contact?.let { contactRepository.deleteContact(it.id) }
    }

    fun onToggleFavorite() = launch(R.string.error_contact_favorite_change_failed) {
        uiState.value.contact?.let { contactRepository.setFavorite(it.id, !it.isFavorite) }
    }

    fun onToggleBlocked() = launch(R.string.error_contact_block_change_failed) {
        val state = uiState.value
        val number = state.number ?: return@launch
        if (state.isBlocked == true) blockedRepository.unblock(number) else blockedRepository.block(number)
        blockedRefresh.update { it + 1 }
    }

    fun onMessageShown() {
        message.value = null
    }

    private fun detailsOf(phones: List<Phone>, details: ContactDetails) = Details(
        phones = phones.distinctBy { it.number },
        emails = details.emails,
        organization = details.organization,
        whatsappNumber = details.whatsappNumber,
    )

    private fun callerOf(target: CallerTarget): Flow<Caller?> = when (target) {
        is CallerTarget.ContactTarget -> contactCaller(target.contactId)
        is CallerTarget.LinkTarget -> flow { emit(contactRepository.contactIdOf(target.link)) }
            .flatMapLatest { contactId -> contactId?.let(::contactCaller) ?: flowOf(null) }
        is CallerTarget.RecentTarget -> recentRepository.recent(target.recentId).flatMapLatest { recent ->
            when {
                recent == null -> flowOf(null)
                else -> phoneRepository.lookup(recent.number)?.contactId?.let(::contactCaller)
                    ?: flowOf(Caller(name = recent.name, number = recent.number, contact = null))
            }
        }
    }

    private fun contactCaller(contactId: Long): Flow<Caller?> =
        contactRepository.contact(contactId).map { contact ->
            contact?.let { Caller(name = it.name, number = it.number, contact = it) }
        }

    private fun launch(@StringRes errorMessage: Int, action: suspend () -> Unit) {
        viewModelScope.launch {
            runCatching { action() }.onFailure { error ->
                Log.e(TAG, "Caller action failed", error)
                message.value = errorMessage
            }
        }
    }

    private companion object {
        const val TAG = "CallerViewModel"
    }
}
