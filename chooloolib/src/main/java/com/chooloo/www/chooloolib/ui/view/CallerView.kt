package com.chooloo.www.chooloolib.ui.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.chooloo.www.chooloolib.ui.compose.BottomSheet
import com.chooloo.www.chooloolib.ui.compose.Caller
import com.chooloo.www.chooloolib.ui.viewmodel.accounts.AccountsListViewModelImpl
import com.chooloo.www.chooloolib.ui.viewmodel.caller.CallerViewModelImpl
import com.chooloo.www.chooloolib.ui.viewmodel.phones.PhonesListViewModelImpl
import com.chooloo.www.chooloolib.utils.showVMError

@Composable
fun CallerView(
    recentId: Long? = null,
    contactId: Long? = null,
    callerViewModel: CallerViewModelImpl = hiltViewModel(),
    phonesViewModel: PhonesListViewModelImpl = hiltViewModel(),
    accountsViewModel: AccountsListViewModelImpl = hiltViewModel()
) {
    val context = LocalContext.current
    val uiState by callerViewModel.uiState.collectAsState()
    val phonesUiState by phonesViewModel.uiState.collectAsState()
    val accountsUiState by accountsViewModel.uiState.collectAsState()
    val callerError by callerViewModel.errorFlow.collectAsState(null)
    val phonesError by phonesViewModel.errorFlow.collectAsState(null)
    val accountsError by accountsViewModel.errorFlow.collectAsState(null)

    LaunchedEffect(key1 = recentId) {
        recentId?.let(callerViewModel::onRecentId)
    }

    LaunchedEffect(key1 = contactId) {
        contactId?.let(callerViewModel::onContactId)
    }

    LaunchedEffect(true) {
        uiState.contactData?.id?.let {
            phonesViewModel.onContactId(it)
            accountsViewModel.onContactId(it)
        }

        callerError?.let { showVMError(context, it) }
        phonesError?.let { showVMError(context, it) }
        accountsError?.let { showVMError(context, it) }
    }

    BottomSheet(
        visible = uiState.isHistoryVisible,
        onDismiss = { callerViewModel.onHistoryDismiss() }
    ) {
        RecentsView(filter = uiState.number)
    }

    Caller(
        name = uiState.name,
        number = uiState.number,
        phones = phonesUiState.items,
        isBlocked = uiState.isBlocked,
        accounts = accountsUiState.items,
        loadingState = uiState.loadingState,
        imagePainter = null,// TODO add image
        onPhoneClick = phonesViewModel::onItemClick,
        onAccountClick = accountsViewModel::onItemClick,
        phonesLoadingState = phonesUiState.loadingState,
        accountsLoadingState = accountsUiState.loadingState,
        isContactFavorite = uiState.contactData?.isFavorite == true,
        onSmsClick = if (uiState.isSmsEnabled) callerViewModel::onSmsClick else null,
        onCallClick = if (uiState.isCallable) callerViewModel::onCallClick else null,
        onEditContactClick = if (uiState.isEditable) callerViewModel::onEditClick else null,
        onHistoryClick = if (uiState.isHistorable) callerViewModel::onHistoryShow else null,
        onSetBlockedClick = if (uiState.isBlockable == true) callerViewModel::onToggleBlocked else null,
        onDeleteContactClick = if (uiState.isDeletable) callerViewModel::onDeleteClick else null,
        onSetContactFavoriteClick = if (uiState.isFavorable) callerViewModel::onToggleFavorite else null,
        onCreateContactClick = if (uiState.contactData == null) callerViewModel::onCreateContactClick else null
    )
}