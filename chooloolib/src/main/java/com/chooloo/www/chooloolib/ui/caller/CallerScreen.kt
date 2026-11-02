package com.chooloo.www.chooloolib.ui.caller

import android.widget.Toast
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

@Composable
fun CallerScreen(
    target: CallerTarget,
    viewModel: CallerViewModel = hiltViewModel<CallerViewModel, CallerViewModel.Factory>(
        key = target.toString(),
        creationCallback = { factory -> factory.create(target) }
    ),
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val contact = uiState.contact
    val number = uiState.number

    val defaultPainter = rememberVectorPainter(Icons.Rounded.Person)
    val avatarPainter = if (contact?.photoUri != null) {
        rememberAsyncImagePainter(model = contact.photoUri, error = defaultPainter, placeholder = defaultPainter)
    } else {
        defaultPainter
    }

    LaunchedEffect(uiState.message) {
        uiState.message?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.onMessageShown()
        }
    }

    BottomSheet(visible = uiState.isHistoryVisible, onDismiss = viewModel::onHistoryDismiss) {
        RecentsScreen(filter = number, viewModel = hiltViewModel(key = "history-$target"))
    }

    Caller(
        name = uiState.name,
        number = number,
        phones = uiState.phones,
        accounts = uiState.accounts,
        isBlocked = uiState.isBlocked,
        loadingState = uiState.loadingState,
        imagePainter = avatarPainter,
        onPhoneClick = viewModel::onPhoneClick,
        onPhoneLongClick = { context.copyToClipboard(it.number) },
        onAccountClick = { account ->
            if (account.type == ContactAccount.Type.WHATSAPP) account.data?.let(context::openWhatsapp)
        },
        isContactFavorite = contact?.isFavorite == true,
        onSmsClick = number?.takeIf { uiState.hasNumber }?.let { { context.openSms(it) } },
        onCallClick = if (uiState.hasNumber) viewModel::onCallClick else null,
        onEditContactClick = contact?.let { { context.editContact(it.id) } },
        onHistoryClick = viewModel::onHistoryShow,
        onSetBlockedClick = if (uiState.hasNumber) viewModel::onToggleBlocked else null,
        onDeleteContactClick = contact?.let { viewModel::onDeleteClick },
        onSetContactFavoriteClick = contact?.let { viewModel::onToggleFavorite },
        onCreateContactClick = if (contact == null) ({ context.createContact(number) }) else null
    )
}
