package com.chooloo.www.chooloolib.ui.caller

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.domain.model.Contact
import com.chooloo.www.chooloolib.domain.model.Recent
import com.chooloo.www.chooloolib.domain.repository.BlockedRepository
import com.chooloo.www.chooloolib.domain.repository.ContactRepository
import com.chooloo.www.chooloolib.domain.repository.PhoneRepository
import com.chooloo.www.chooloolib.domain.repository.RecentRepository
import com.chooloo.www.chooloolib.domain.repository.TelecomRepository
import com.chooloo.www.chooloolib.ui.common.BaseViewModel
import com.chooloo.www.chooloolib.utils.LoadingState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class CallerViewModel @Inject constructor(
    private val phoneRepository: PhoneRepository,
    private val recentRepository: RecentRepository,
    private val blockedRepository: BlockedRepository,
    private val contactRepository: ContactRepository,
    private val telecomRepository: TelecomRepository,
) : BaseViewModel() {
    data class UiState(
        val name: String? = null,
        val number: String? = null,
        val contact: Contact? = null,
        val isBlocked: Boolean? = null,
        val isDeleted: Boolean = false,
        val isHistoryVisible: Boolean = false,
        val loadingState: LoadingState = LoadingState.IDLE,
    ) {
        val hasNumber: Boolean get() = !number.isNullOrEmpty()
    }

    private var _loadJob: Job? = null
    private val _uiState = MutableStateFlow(UiState())

    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    fun onContactId(contactId: Long) {
        load {
            contactRepository.contact(contactId)
                .catch { onLoadFailed(it, Errors.ContactNotFound()) }
                .collect { contact ->
                    if (contact == null) {
                        onLoadFailed(null, Errors.ContactNotFound())
                    } else {
                        showCaller(name = contact.name, number = contact.number, contact = contact)
                    }
                }
        }
    }

    fun onRecentId(recentId: Long) {
        load {
            recentRepository.recent(recentId)
                .catch { onLoadFailed(it, Errors.RecentNotFound()) }
                .collect { recent ->
                    if (recent == null) {
                        onLoadFailed(null, Errors.RecentNotFound())
                    } else {
                        showRecent(recent)
                    }
                }
        }
    }

    fun onCallClick() {
        _uiState.value.number?.let { viewModelScope.launch { telecomRepository.callNumber(it) } }
    }

    fun onDeleteClick() {
        val contact = _uiState.value.contact ?: return
        viewModelScope.launch {
            runCatching { contactRepository.deleteContact(contact.id) }
                .onSuccess { _uiState.update { it.copy(isDeleted = true) } }
                .onFailure { onActionFailed(it, Errors.ContactDeletionFailed()) }
        }
    }

    fun onToggleBlocked(isBlocked: Boolean) {
        val number = _uiState.value.number ?: return
        viewModelScope.launch {
            runCatching {
                if (isBlocked) blockedRepository.block(number) else blockedRepository.unblock(number)
                blockedRepository.isBlocked(number)
            }
                .onSuccess { blocked -> _uiState.update { it.copy(isBlocked = blocked) } }
                .onFailure { onActionFailed(it, Errors.ContactBlockChangeFailed()) }
        }
    }

    fun onToggleFavorite(isFavorite: Boolean) {
        val contact = _uiState.value.contact ?: return
        viewModelScope.launch {
            runCatching { contactRepository.setFavorite(contact.id, isFavorite) }
                .onFailure { onActionFailed(it, Errors.ContactFavChangeFailed()) }
        }
    }

    fun onHistoryShow() {
        _uiState.update { it.copy(isHistoryVisible = true) }
    }

    fun onHistoryDismiss() {
        _uiState.update { it.copy(isHistoryVisible = false) }
    }

    private fun load(block: suspend () -> Unit) {
        _loadJob?.cancel()
        _uiState.update { UiState(loadingState = LoadingState.LOADING) }
        _loadJob = viewModelScope.launch { block() }
    }

    private suspend fun showRecent(recent: Recent) {
        val contactId = phoneRepository.lookup(recent.number)?.contactId
        if (contactId != null) {
            onContactId(contactId)
        } else {
            showCaller(name = recent.name, number = recent.number, contact = null)
        }
    }

    private suspend fun showCaller(name: String?, number: String?, contact: Contact?) {
        _uiState.update {
            it.copy(name = name, number = number, contact = contact, loadingState = LoadingState.SUCCESS)
        }
        if (!number.isNullOrEmpty() && telecomRepository.isDefaultDialer()) {
            val isBlocked = blockedRepository.isBlocked(number)
            _uiState.update { it.copy(isBlocked = isBlocked) }
        }
    }

    private fun onLoadFailed(error: Throwable?, vmError: VMError) {
        error?.let { Log.e(TAG, "Failed loading caller", it) }
        _uiState.update { it.copy(loadingState = LoadingState.FAILED) }
        onError(vmError)
    }

    private fun onActionFailed(error: Throwable, vmError: VMError) {
        Log.e(TAG, "Caller action failed", error)
        onError(vmError)
    }

    sealed class Errors {
        class RecentNotFound : VMError(R.string.error_recent_not_found)
        class ContactNotFound : VMError(R.string.error_contact_not_found)
        class ContactDeletionFailed : VMError(R.string.error_contact_deletion_failed)
        class ContactBlockChangeFailed : VMError(R.string.error_contact_block_change_failed)
        class ContactFavChangeFailed : VMError(R.string.error_contact_favorite_change_failed)
    }

    private companion object {
        const val TAG = "CallerViewModel"
    }
}
