package com.chooloo.www.chooloolib.ui.caller

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chooloo.www.chooloolib.domain.model.Contact
import com.chooloo.www.chooloolib.domain.model.ContactDetails
import com.chooloo.www.chooloolib.domain.model.Recent
import com.chooloo.www.chooloolib.domain.repository.BlockedRepository
import com.chooloo.www.chooloolib.domain.repository.ContactRepository
import com.chooloo.www.chooloolib.domain.repository.PhoneRepository
import com.chooloo.www.chooloolib.domain.repository.RecentRepository
import com.chooloo.www.chooloolib.domain.repository.TelecomRepository
import com.chooloo.www.chooloolib.ui.common.LoadingState
import com.chooloo.www.chooloolib.ui.common.UserMessage
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

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel(assistedFactory = CallerViewModel.Factory::class)
class CallerViewModel @AssistedInject constructor(
    @Assisted target: CallerTarget,
    private val phoneRepository: PhoneRepository,
    private val recentRepository: RecentRepository,
    private val blockedRepository: BlockedRepository,
    private val contactRepository: ContactRepository,
    private val telecomRepository: TelecomRepository,
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
        val details: ContactDetails = ContactDetails(),
        val history: List<Recent> = emptyList(),
        val isBlocked: Boolean? = null,
        val message: UserMessage? = null,
    ) {
        val hasNumber: Boolean get() = !number.isNullOrEmpty()
    }

    private data class Caller(val name: String?, val number: String?, val contact: Contact?)

    private val message = MutableStateFlow<UserMessage?>(null)
    private val blockedRefresh = MutableStateFlow(0)

    private val caller: Flow<Caller?> = when (target) {
        is CallerTarget.ContactTarget -> contactCaller(target.contactId)

        is CallerTarget.LinkTarget -> flow { emit(contactRepository.contactIdOf(target.link)) }
            .flatMapLatest { contactId -> contactId?.let(::contactCaller) ?: flowOf(null) }

        is CallerTarget.RecentTarget -> recentRepository.recent(target.recentId)
            .flatMapLatest { recent ->
                when (recent) {
                    null -> flowOf(null)

                    else -> phoneRepository.lookup(recent.number)?.contactId?.let(::contactCaller)
                        ?: flowOf(
                            Caller(name = recent.name, number = recent.number, contact = null),
                        )
                }
            }
    }.catch { error ->
        Log.e(TAG, "Failed loading caller", error)
        emit(null)
    }

    private val details: Flow<ContactDetails> = caller.map { it?.contact?.id }
        .distinctUntilChanged()
        .flatMapLatest { contactId ->
            if (contactId == null) {
                flowOf(ContactDetails())
            } else {
                contactRepository.details(contactId).catch { error ->
                    Log.e(TAG, "Failed loading caller details", error)
                    emit(ContactDetails())
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

    private val isBlocked: Flow<Boolean?> = combine(
        caller.map { it?.number }.distinctUntilChanged(),
        blockedRefresh,
    ) { number, _ -> number }
        .mapLatest { number ->
            val blockable =
                number?.takeIf { it.isNotEmpty() && telecomRepository.isDefaultDialer() }
                    ?: return@mapLatest null
            runCatching { blockedRepository.isBlocked(blockable) }
                .onFailure { Log.e(TAG, "Failed reading blocked state", it) }
                .getOrNull()
        }

    val uiState: StateFlow<UiState> = combine(caller, details, history, isBlocked, message) {
            caller,
            details,
            history,
            isBlocked,
            message,
        ->
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

    fun onCall(number: String) = launch(UserMessage.CALL_FAILED) {
        telecomRepository.callNumber(number)
    }

    fun onDeleteConfirmed() = launch(UserMessage.CONTACT_DELETION_FAILED) {
        uiState.value.contact?.let { contactRepository.deleteContact(it.id) }
    }

    fun onToggleFavorite() = launch(UserMessage.CONTACT_FAVORITE_CHANGE_FAILED) {
        uiState.value.contact?.let { contactRepository.setFavorite(it.id, !it.isFavorite) }
    }

    fun onToggleBlocked() = launch(UserMessage.CONTACT_BLOCK_CHANGE_FAILED) {
        val state = uiState.value
        val number = state.number ?: return@launch
        if (state.isBlocked == true) {
            blockedRepository.unblock(number)
        } else {
            blockedRepository.block(number)
        }
        blockedRefresh.update { it + 1 }
    }

    fun onMessageShown() {
        message.value = null
    }

    private fun contactCaller(contactId: Long): Flow<Caller?> =
        contactRepository.contact(contactId).map { contact ->
            contact?.let { Caller(name = it.name, number = it.number, contact = it) }
        }

    private fun launch(errorMessage: UserMessage, action: suspend () -> Unit) {
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
