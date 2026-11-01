package com.chooloo.www.chooloolib.ui.caller

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.rememberAsyncImagePainter
import com.chooloo.www.chooloolib.ui.components.BottomSheet
import com.chooloo.www.chooloolib.ui.recents.RecentsScreen
import com.chooloo.www.chooloolib.utils.showVMError

@Composable
fun CallerScreen(
    recentId: Long? = null,
    contactId: Long? = null,
    callerViewModel: CallerViewModel = hiltViewModel(),
    phonesViewModel: PhonesListViewModel = hiltViewModel(),
    accountsViewModel: AccountsListViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val uiState by callerViewModel.uiState.collectAsStateWithLifecycle()
    val phonesUiState by phonesViewModel.uiState.collectAsStateWithLifecycle()
    val accountsUiState by accountsViewModel.uiState.collectAsStateWithLifecycle()

    val defaultPainter = rememberVectorPainter(Icons.Rounded.Person)
    val avatarPainter = if (uiState.contactData?.imageUri != null) {
        rememberAsyncImagePainter(
            model = uiState.contactData?.imageUri,
            error = defaultPainter,
            placeholder = defaultPainter
        )
    } else {
        defaultPainter
    }

    LaunchedEffect(key1 = recentId) {
        recentId?.let(callerViewModel::onRecentId)
    }

    LaunchedEffect(key1 = contactId) {
        contactId?.let(callerViewModel::onContactId)
    }

    LaunchedEffect(key1 = uiState.contactData?.id) {
        uiState.contactData?.id?.let {
            phonesViewModel.onContactId(it)
            accountsViewModel.onContactId(it)
        }
    }

    LaunchedEffect(callerViewModel, phonesViewModel, accountsViewModel) {
        callerViewModel.errorFlow.collect { showVMError(context, it) }
    }

    LaunchedEffect(phonesViewModel) {
        phonesViewModel.errorFlow.collect { showVMError(context, it) }
    }

    LaunchedEffect(accountsViewModel) {
        accountsViewModel.errorFlow.collect { showVMError(context, it) }
    }

    BottomSheet(
        visible = uiState.isHistoryVisible,
        onDismiss = { callerViewModel.onHistoryDismiss() }
    ) {
        RecentsScreen(filter = uiState.number)
    }

    Caller(
        name = uiState.name,
        number = uiState.number,
        phones = phonesUiState.items,
        isBlocked = uiState.isBlocked,
        accounts = accountsUiState.items,
        loadingState = uiState.loadingState,
        imagePainter = avatarPainter,
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
