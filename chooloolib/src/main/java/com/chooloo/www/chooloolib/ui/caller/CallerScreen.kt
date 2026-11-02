package com.chooloo.www.chooloolib.ui.caller

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.ui.common.copyToClipboard
import com.chooloo.www.chooloolib.ui.common.createContact
import com.chooloo.www.chooloolib.ui.common.editContact
import com.chooloo.www.chooloolib.ui.common.openSms
import com.chooloo.www.chooloolib.ui.common.openWhatsapp
import com.chooloo.www.chooloolib.ui.common.sendEmail
import com.chooloo.www.chooloolib.ui.components.ConfirmDialog
import com.chooloo.www.chooloolib.ui.components.MessageToast

@Composable
fun CallerScreen(
    target: CallerTarget,
    onDismiss: () -> Unit,
    viewModel: CallerViewModel = hiltViewModel<CallerViewModel, CallerViewModel.Factory>(
        key = target.toString(),
        creationCallback = { factory -> factory.create(target) }
    ),
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var isDeleteConfirming by rememberSaveable { mutableStateOf(false) }
    var isBlockConfirming by rememberSaveable { mutableStateOf(false) }

    MessageToast(message = uiState.message, onShown = viewModel::onMessageShown)

    Caller(
        state = uiState,
        onCall = viewModel::onCall,
        onMessage = context::openSms,
        onWhatsapp = context::openWhatsapp,
        onEmail = context::sendEmail,
        onCopy = context::copyToClipboard,
        onToggleFavorite = viewModel::onToggleFavorite,
        onToggleBlocked = { if (uiState.isBlocked == true) viewModel.onToggleBlocked() else isBlockConfirming = true },
        onEdit = context::editContact,
        onAddContact = context::createContact,
        onDelete = { isDeleteConfirming = true },
    )

    if (isBlockConfirming) {
        ConfirmDialog(
            title = stringResource(R.string.confirm_block_title),
            text = stringResource(R.string.confirm_block),
            confirmLabel = stringResource(R.string.action_block),
            onConfirm = viewModel::onToggleBlocked,
            onDismiss = { isBlockConfirming = false },
        )
    }

    if (isDeleteConfirming) {
        ConfirmDialog(
            title = stringResource(R.string.confirm_delete_contact_title),
            text = stringResource(R.string.confirm_delete_contact),
            confirmLabel = stringResource(R.string.action_delete),
            onConfirm = {
                viewModel.onDeleteConfirmed()
                onDismiss()
            },
            onDismiss = { isDeleteConfirming = false },
        )
    }
}
