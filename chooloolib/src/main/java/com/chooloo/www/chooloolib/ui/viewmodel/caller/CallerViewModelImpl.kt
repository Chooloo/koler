package com.chooloo.www.chooloolib.ui.viewmodel.caller

import androidx.lifecycle.viewModelScope
import com.chooloo.www.chooloolib.domain.model.ContactData
import com.chooloo.www.chooloolib.domain.model.RecentData
import com.chooloo.www.chooloolib.domain.repository.blocked.BlockedRepository
import com.chooloo.www.chooloolib.domain.repository.contact.ContactRepository
import com.chooloo.www.chooloolib.domain.repository.navigation.NavigationRepository
import com.chooloo.www.chooloolib.domain.repository.permission.PermissionRepository
import com.chooloo.www.chooloolib.domain.repository.phone.PhoneRepository
import com.chooloo.www.chooloolib.domain.repository.recent.RecentRepository
import com.chooloo.www.chooloolib.domain.repository.telecom.TelecomRepository
import com.chooloo.www.chooloolib.ui.viewmodel.base.BaseViewModelImpl
import com.chooloo.www.chooloolib.ui.viewmodel.caller.CallerViewModel.Companion.UiState
import com.chooloo.www.chooloolib.ui.viewmodel.caller.CallerViewModel.Errors
import com.chooloo.www.chooloolib.utils.LoadingState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CallerViewModelImpl @Inject constructor(
    private val phoneRepository: PhoneRepository,
    private val recentRepository: RecentRepository,
    private val blockedRepository: BlockedRepository,
    private val contactRepository: ContactRepository,
    private val telecomRepository: TelecomRepository,
    private val naviationRepository: NavigationRepository,
    private val permissionRepository: PermissionRepository
) : BaseViewModelImpl(), CallerViewModel {
    private val _uiState = MutableStateFlow(UiState())

    override val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private fun ensureContact(): ContactData? = _uiState.value.contactData.run {
        if (this != ContactData.UNKNOWN) {
            this
        } else {
            onError(Errors.ContactNotFound())
            null
        }
    }

    private fun ensureNumber(): String? = _uiState.value.number ?: run {
        onError(Errors.ContactNumberNotFound())
        null
    }

    private fun setContactItem(contactData: ContactData) {
        _uiState.update {
            it.copy(
                isFavorable = true,
                isHistorable = true,
                contactData = contactData,
                loadingState = LoadingState.SUCCESS,
                isCallable = contactData.number != null,
                isBlockable = contactData.number != null,
                isSmsEnabled = contactData.number != null,
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
                            isCallable = true,
                            isBlockable = true,
                            isEditable = false,
                            isDeletable = false,
                            isSmsEnabled = true,
                            isHistorable = true,
                            isFavorable = false,
                            recentData = recentData,
                            number = recentData.number,
                            loadingState = LoadingState.SUCCESS,
                        )
                    }
                    _uiState.update {
                        it.copy(isBlocked = blockedRepository.isNumberBlocked(recentData.number))
                    }
                }
            }
        }
    }

    override fun onSmsClick() {
        ensureNumber()?.let {
            viewModelScope.launch { naviationRepository.goToSendSms(it) }
        }
    }

    override fun onCallClick() {
        ensureContact()?.number?.let {
            viewModelScope.launch { telecomRepository.callNumber(it) }
        }
    }

    override fun onEditClick() {
        ensureContact()?.id?.let {
            viewModelScope.launch { contactRepository.editContact(it) }
        }
    }

    override fun onDeleteClick() {
        ensureContact()?.let {
            viewModelScope.launch {
                try {
                    contactRepository.deleteContact(it.id)
                    _uiState.update { it.copy(isDeleted = true) }
                } catch (e: Exception) {
                    onError(Errors.ContactDeletionFailed())
                }
            }
        }
    }

    override fun onHistoryShow() {
        _uiState.update { it.copy(isHistoryVisible = true) }
    }

    override fun onHistoryDismiss() {
        _uiState.update { it.copy(isHistoryVisible = false) }
    }

    override fun onCreateContactClick() {
        viewModelScope.launch {
            ensureNumber()?.let { contactRepository.createContact(it) }
        }
    }

    override fun onRecentId(recentId: Long) {
        _uiState.update { it.copy(loadingState = LoadingState.LOADING) }
        viewModelScope.launch {
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

    override fun onContactId(contactId: Long) {
        _uiState.update { it.copy(loadingState = LoadingState.LOADING) }
        viewModelScope.launch {
            contactRepository.getContactFlow(contactId).collect { contact ->
                contact?.id?.let {
                    phoneRepository.getContactAccounts(contact.id).firstOrNull()?.let { phone ->
                        setContactItem(ContactData.fromRecord(contact, phone.number))
                    } ?: run {
                        setContactItem(ContactData.fromRecord(contact))
                    }
                } ?: run {
                    _uiState.update { it.copy(loadingState = LoadingState.FAILED) }
                    onError(Errors.ContactNotFound())
                }
            }
        }
    }

    override fun onToggleBlocked(isBlocked: Boolean) {
        ensureNumber()?.let {
            viewModelScope.launch {
                try {
                    if (isBlocked) {
                        blockedRepository.blockNumber(it)
                    } else {
                        blockedRepository.unblockNumber(it)
                    }
                } catch (e: Exception) {
                    onError(Errors.ContactBlockChangeFailed())
                }
            }
        }
    }

    override fun onToggleFavorite(isFavorite: Boolean) {
        ensureContact()?.let {
            viewModelScope.launch {
                try {
                    contactRepository.toggleContactFavorite(it.id, isFavorite)
                } catch (e: Exception) {
                    onError(Errors.ContactFavChangeFailed())
                }
            }
        }
    }
}