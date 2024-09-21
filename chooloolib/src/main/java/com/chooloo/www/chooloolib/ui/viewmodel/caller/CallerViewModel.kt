package com.chooloo.www.chooloolib.ui.viewmodel.caller

import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.domain.model.ContactData
import com.chooloo.www.chooloolib.domain.model.RecentData
import com.chooloo.www.chooloolib.ui.viewmodel.base.BaseViewModel
import com.chooloo.www.chooloolib.ui.viewmodel.base.BaseViewModel.VMError
import com.chooloo.www.chooloolib.utils.LoadingState
import kotlinx.coroutines.flow.StateFlow

interface CallerViewModel : BaseViewModel {
    companion object {
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
    }

    val uiState: StateFlow<UiState>

    fun onSmsClick()
    fun onCallClick()
    fun onEditClick()
    fun onDeleteClick()
    fun onHistoryShow()
    fun onHistoryDismiss()
    fun onCreateContactClick()
    fun onRecentId(recentId: Long)
    fun onContactId(contactId: Long)
    fun onToggleBlocked(isBlocked: Boolean)
    fun onToggleFavorite(isFavorite: Boolean)

    sealed class Errors {
        class RecentNotFound : VMError(R.string.error_recent_not_found)
        class ContactNotFound : VMError(R.string.error_contact_not_found)
        class ContactDeletionFailed : VMError(R.string.error_contact_deletion_failed)
        class ContactNumberNotFound : VMError(R.string.error_contact_number_not_found)
        class ContactBlockChangeFailed : VMError(R.string.error_contact_block_change_failed)
        class ContactFavChangeFailed : VMError(R.string.error_contact_favorite_change_failed)
    }
}