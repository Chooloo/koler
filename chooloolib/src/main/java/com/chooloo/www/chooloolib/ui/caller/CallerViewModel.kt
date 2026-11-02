package com.chooloo.www.chooloolib.ui.caller

import android.util.Log
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.domain.model.Contact
import com.chooloo.www.chooloolib.domain.model.ContactAccount
import com.chooloo.www.chooloolib.domain.model.Phone
import com.chooloo.www.chooloolib.domain.repository.BlockedRepository
import com.chooloo.www.chooloolib.domain.repository.ContactAccountRepository
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
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface CallerTarget {
    data class ContactTarget(val contactId: Long) : CallerTarget
    data class RecentTarget(val recentId: Long) : CallerTarget
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
    private val contactAccountRepository: ContactAccountRepository,
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
        val phones: List<Phone> = emptyList(),
        val accounts: List<ContactAccount> = emptyList(),
        val isBlocked: Boolean? = null,
        val isHistoryVisible: Boolean = false,
        @StringRes val message: Int? = null,
    ) {
        val hasNumber: Boolean get() = !number.isNullOrEmpty()
    }

    private data class Caller(val name: String?, val number: String?, val contact: Contact?)

    private data class Details(val phones: List<Phone> = emptyList(), val accounts: List<ContactAccount> = emptyList())

    private data class LocalState(val isHistoryVisible: Boolean = false, @StringRes val message: Int? = null)

    private sealed interface Load {
        data object Failed : Load
        data class Found(val caller: Caller) : Load
    }

    private val localState = MutableStateFlow(LocalState())
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
                combine(phoneRepository.phones(contactId), contactAccountRepository.accounts(contactId), ::Details)
                    .catch { error ->
                        Log.e(TAG, "Failed loading caller details", error)
                        emit(Details())
                    }
            }
        }

    private val isBlocked: Flow<Boolean?> = combine(caller.map { it?.number }.distinctUntilChanged(), blockedRefresh) { number, _ -> number }
        .mapLatest { number ->
            val blockable = number?.takeIf { it.isNotEmpty() && telecomRepository.isDefaultDialer() } ?: return@mapLatest null
            runCatching { blockedRepository.isBlocked(blockable) }
                .onFailure { Log.e(TAG, "Failed reading blocked state", it) }
                .getOrNull()
        }

    val uiState: StateFlow<UiState> = combine(load, details, isBlocked, localState) { load, details, isBlocked, local ->
        val caller = (load as? Load.Found)?.caller
        UiState(
            loadingState = when (load) {
                Load.Failed -> LoadingState.FAILED
                is Load.Found -> LoadingState.SUCCESS
            },
            name = caller?.name,
            number = caller?.number,
            contact = caller?.contact,
            phones = details.phones,
            accounts = details.accounts,
            isBlocked = isBlocked,
            isHistoryVisible = local.isHistoryVisible,
            message = local.message,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UiState())

    fun onCallClick() = launch(R.string.error_contact_number_not_found) {
        uiState.value.number?.let { telecomRepository.callNumber(it) }
    }

    fun onPhoneClick(phone: Phone) = launch(R.string.error_contact_number_not_found) {
        telecomRepository.callNumber(phone.number)
    }

    fun onDeleteClick() = launch(R.string.error_contact_deletion_failed) {
        uiState.value.contact?.let { contactRepository.deleteContact(it.id) }
    }

    fun onToggleFavorite(isFavorite: Boolean) = launch(R.string.error_contact_favorite_change_failed) {
        uiState.value.contact?.let { contactRepository.setFavorite(it.id, isFavorite) }
    }

    fun onToggleBlocked(isBlocked: Boolean) = launch(R.string.error_contact_block_change_failed) {
        val number = uiState.value.number ?: return@launch
        if (isBlocked) blockedRepository.block(number) else blockedRepository.unblock(number)
        blockedRefresh.update { it + 1 }
    }

    fun onHistoryShow() = localState.update { it.copy(isHistoryVisible = true) }

    fun onHistoryDismiss() = localState.update { it.copy(isHistoryVisible = false) }

    fun onMessageShown() = localState.update { it.copy(message = null) }

    private fun callerOf(target: CallerTarget): Flow<Caller?> = when (target) {
        is CallerTarget.ContactTarget -> contactCaller(target.contactId)
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
                localState.update { it.copy(message = errorMessage) }
            }
        }
    }

    private companion object {
        const val TAG = "CallerViewModel"
    }
}
