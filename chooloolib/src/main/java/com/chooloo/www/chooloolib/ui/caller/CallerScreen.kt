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
import com.chooloo.www.chooloolib.domain.model.ContactAccount
import com.chooloo.www.chooloolib.ui.common.copyToClipboard
import com.chooloo.www.chooloolib.ui.common.createContact
import com.chooloo.www.chooloolib.ui.common.editContact
import com.chooloo.www.chooloolib.ui.common.openSms
import com.chooloo.www.chooloolib.ui.common.openWhatsapp
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
    val contact = uiState.contact
    val number = uiState.number

    val defaultPainter = rememberVectorPainter(Icons.Rounded.Person)
    val avatarPainter = if (contact?.photoUri != null) {
        rememberAsyncImagePainter(model = contact.photoUri, error = defaultPainter, placeholder = defaultPainter)
    } else {
        defaultPainter
    }

    LaunchedEffect(key1 = recentId) {
        recentId?.let(callerViewModel::onRecentId)
    }

    LaunchedEffect(key1 = contactId) {
        contactId?.let(callerViewModel::onContactId)
    }

    LaunchedEffect(key1 = contact?.id) {
        contact?.id?.let {
            phonesViewModel.onContactId(it)
            accountsViewModel.onContactId(it)
        }
    }

    LaunchedEffect(callerViewModel) {
        callerViewModel.errorFlow.collect { showVMError(context, it) }
    }

    BottomSheet(
        visible = uiState.isHistoryVisible,
        onDismiss = callerViewModel::onHistoryDismiss
    ) {
        RecentsScreen(filter = number)
    }

    Caller(
        name = uiState.name,
        number = number,
        phones = phonesUiState.items,
        isBlocked = uiState.isBlocked,
        accounts = accountsUiState.items,
        loadingState = uiState.loadingState,
        imagePainter = avatarPainter,
        onPhoneClick = phonesViewModel::onItemClick,
        onPhoneLongClick = { context.copyToClipboard(it.number) },
        onAccountClick = { account ->
            if (account.type == ContactAccount.Type.WHATSAPP) account.data?.let(context::openWhatsapp)
        },
        phonesLoadingState = phonesUiState.loadingState,
        accountsLoadingState = accountsUiState.loadingState,
        isContactFavorite = contact?.isFavorite == true,
        onSmsClick = number?.takeIf { uiState.hasNumber }?.let { { context.openSms(it) } },
        onCallClick = if (uiState.hasNumber) callerViewModel::onCallClick else null,
        onEditContactClick = contact?.let { { context.editContact(it.id) } },
        onHistoryClick = callerViewModel::onHistoryShow,
        onSetBlockedClick = if (uiState.hasNumber) callerViewModel::onToggleBlocked else null,
        onDeleteContactClick = contact?.let { callerViewModel::onDeleteClick },
        onSetContactFavoriteClick = contact?.let { callerViewModel::onToggleFavorite },
        onCreateContactClick = if (contact == null) ({ context.createContact(number) }) else null
    )
}
