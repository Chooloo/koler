package com.chooloo.www.chooloolib.ui.caller

import androidx.lifecycle.viewModelScope
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.domain.model.ContactData
import com.chooloo.www.chooloolib.domain.model.RecentData
import com.chooloo.www.chooloolib.domain.repository.blocked.BlockedRepository
import com.chooloo.www.chooloolib.domain.repository.contact.ContactRepository
import com.chooloo.www.chooloolib.domain.repository.navigation.NavigationRepository
import com.chooloo.www.chooloolib.domain.repository.permission.PermissionRepository
import com.chooloo.www.chooloolib.domain.repository.phone.PhoneRepository
import com.chooloo.www.chooloolib.domain.repository.recent.RecentRepository
import com.chooloo.www.chooloolib.domain.repository.telecom.TelecomRepository
import com.chooloo.www.chooloolib.ui.common.BaseViewModel
import com.chooloo.www.chooloolib.utils.LoadingState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class CallerViewModel @Inject constructor(
    private val phoneRepository: PhoneRepository,
    private val recentRepository: RecentRepository,
    private val blockedRepository: BlockedRepository,
    private val contactRepository: ContactRepository,
    private val telecomRepository: TelecomRepository,
    private val navigationRepository: NavigationRepository,
    private val permissionRepository: PermissionRepository
) : BaseViewModel() {
    data class UiState(
        val name: String? = null,
        val number: String? = null,
        val isBlocked: Boolean? = null,
        val isDeleted: Boolean = false,
        val isCallable: Boolean = false,
        val isEditable: Boolean = false,
        val isBlockable: Boolean? = null,
        val isDeletable: Boolean = false,
        val isFavorable: Boolean = false,
        val isHistorable: Boolean = false,
        val isSmsEnabled: Boolean = false,
        val recentData: RecentData? = null,
        val contactData: ContactData? = null,
        val isHistoryVisible: Boolean = false,
        val loadingState: LoadingState = LoadingState.IDLE,
    )

    private var _loadJob: Job? = null
    private val _uiState = MutableStateFlow(UiState())

    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private fun ensureContact(): ContactData? = _uiState.value.contactData?.let {
        if (it != ContactData.UNKNOWN) it else {
            onError(Errors.ContactNotFound())
            null
        }
    } ?: run {
        onError(Errors.ContactNotFound())
        null
    }

    private fun ensureNumber(): String? = _uiState.value.number ?: run {
        onError(Errors.ContactNumberNotFound())
        null
    }

    private fun setContactItem(contactData: ContactData) {
        _uiState.update {
            it.copy(
                name = contactData.name,
                number = contactData.number,
                isFavorable = true,
                isHistorable = true,
                contactData = contactData,
                loadingState = LoadingState.SUCCESS,
                isCallable = !contactData.number.isNullOrEmpty(),
                isBlockable = !contactData.number.isNullOrEmpty(),
                isSmsEnabled = !contactData.number.isNullOrEmpty(),
                isEditable = contactData != ContactData.UNKNOWN,
                isDeletable = contactData != ContactData.UNKNOWN,
            )
        }
        if (permissionRepository.isDefaultDialer) {
            contactData.number?.let { number ->
                viewModelScope.launch {
                    _uiState.update {
                        it.copy(isBlocked = blockedRepository.isNumberBlocked(number))
                    }
                }
            }
        }
    }

    private fun setRecentItem(recentData: RecentData) {
        viewModelScope.launch {
            phoneRepository.lookupAccount(recentData.number).also { lookupAccount ->
                lookupAccount?.contactId?.let(::onContactId) ?: run {
                    _uiState.update {
                        it.copy(
                            name = recentData.name,
                            number = recentData.number,
                            recentData = recentData,
                            contactData = null,
                            isCallable = recentData.number.isNotEmpty(),
                            isBlockable = recentData.number.isNotEmpty(),
                            isEditable = false,
                            isDeletable = false,
                            isSmsEnabled = recentData.number.isNotEmpty(),
                            isHistorable = true,
                            isFavorable = false,
                            loadingState = LoadingState.SUCCESS,
                        )
                    }
                    if (permissionRepository.isDefaultDialer && recentData.number.isNotEmpty()) {
                        _uiState.update {
                            it.copy(isBlocked = blockedRepository.isNumberBlocked(recentData.number))
                        }
                    }
                }
            }
        }
    }

    fun onSmsClick() {
        ensureNumber()?.let {
            viewModelScope.launch { navigationRepository.goToSendSms(it) }
        }
    }

    fun onCallClick() {
        ensureNumber()?.let {
            viewModelScope.launch { telecomRepository.callNumber(it) }
        }
    }

    fun onEditClick() {
        ensureContact()?.id?.let {
            viewModelScope.launch { contactRepository.editContact(it) }
        }
    }

    fun onDeleteClick() {
        ensureContact()?.let { contact ->
            viewModelScope.launch {
                try {
                    contactRepository.deleteContact(contact.id)
                    _uiState.update { it.copy(isDeleted = true) }
                } catch (_: Exception) {
                    onError(Errors.ContactDeletionFailed())
                }
            }
        }
    }

    fun onHistoryShow() {
        _uiState.update { it.copy(isHistoryVisible = true) }
    }

    fun onHistoryDismiss() {
        _uiState.update { it.copy(isHistoryVisible = false) }
    }

    fun onCreateContactClick() {
        viewModelScope.launch {
            ensureNumber()?.let { contactRepository.createContact(it) }
        }
    }

    fun onRecentId(recentId: Long) {
        _loadJob?.cancel()
        _uiState.update { it.copy(loadingState = LoadingState.LOADING, isDeleted = false) }
        _loadJob = viewModelScope.launch {
            recentRepository.getRecentFlow(recentId).collect { record ->
                record?.let {
                    setRecentItem(RecentData.fromRecord(record))
                } ?: run {
                    _uiState.update { it.copy(loadingState = LoadingState.FAILED) }
                    onError(Errors.RecentNotFound())
                }
            }
        }
    }

    fun onContactId(contactId: Long) {
        _loadJob?.cancel()
        _uiState.update { it.copy(loadingState = LoadingState.LOADING, isDeleted = false) }
        _loadJob = viewModelScope.launch {
            contactRepository.getContactFlow(contactId).collect { contact ->
                contact?.id?.let {
                    val phone = phoneRepository.getContactAccounts(contact.id).firstOrNull()
                    setContactItem(ContactData.fromRecord(contact, phone?.number))
                } ?: run {
                    _uiState.update { it.copy(loadingState = LoadingState.FAILED) }
                    onError(Errors.ContactNotFound())
                }
            }
        }
    }

    fun onToggleBlocked(isBlocked: Boolean) {
        ensureNumber()?.let { number ->
            viewModelScope.launch {
                try {
                    if (isBlocked) {
                        blockedRepository.blockNumber(number)
                    } else {
                        blockedRepository.unblockNumber(number)
                    }
                    _uiState.update { it.copy(isBlocked = blockedRepository.isNumberBlocked(number)) }
                } catch (_: Exception) {
                    onError(Errors.ContactBlockChangeFailed())
                }
            }
        }
    }

    fun onToggleFavorite(isFavorite: Boolean) {
        ensureContact()?.let { contact ->
            viewModelScope.launch {
                try {
                    contactRepository.toggleContactFavorite(contact.id, isFavorite)
                    _uiState.update {
                        it.copy(contactData = it.contactData?.copy(isFavorite = isFavorite))
                    }
                } catch (_: Exception) {
                    onError(Errors.ContactFavChangeFailed())
                }
            }
        }
    }

    sealed class Errors {
        class RecentNotFound : VMError(R.string.error_recent_not_found)
        class ContactNotFound : VMError(R.string.error_contact_not_found)
        class ContactDeletionFailed : VMError(R.string.error_contact_deletion_failed)
        class ContactNumberNotFound : VMError(R.string.error_contact_number_not_found)
        class ContactBlockChangeFailed : VMError(R.string.error_contact_block_change_failed)
        class ContactFavChangeFailed : VMError(R.string.error_contact_favorite_change_failed)
    }
}
