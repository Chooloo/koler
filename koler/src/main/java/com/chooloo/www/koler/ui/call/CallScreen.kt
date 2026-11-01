package com.chooloo.www.koler.ui.call

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.rememberAsyncImagePainter
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.ui.components.BottomSheet
import com.chooloo.www.chooloolib.ui.components.DialpadTextField
import com.chooloo.www.chooloolib.ui.components.Keypad
import com.chooloo.www.chooloolib.ui.dialpad.Dialpad
import com.chooloo.www.chooloolib.ui.dialpad.DialpadViewModel

@Composable
fun CallScreen(
    callViewModel: CallViewModel = hiltViewModel(),
    keypadViewModel: KeypadViewModel = hiltViewModel(),
    dialpadViewModel: DialpadViewModel = hiltViewModel()
) {
    val uiState by callViewModel.uiState.collectAsStateWithLifecycle()
    val keypadUiState by keypadViewModel.uiState.collectAsStateWithLifecycle()
    val dialpadUiState by dialpadViewModel.uiState.collectAsStateWithLifecycle()

    val defaultPainter = rememberVectorPainter(Icons.Rounded.Person)
    val callerPainter = if (uiState.callerData.imageUri != null) {
        rememberAsyncImagePainter(
            model = uiState.callerData.imageUri,
            error = defaultPainter,
            placeholder = defaultPainter
        )
    } else {
        defaultPainter
    }

    BottomSheet(visible = uiState.isDialpadOpen, onDismiss = callViewModel::onDismissDialpad) {
        Dialpad(
            value = dialpadUiState.value,
            onCallClick = dialpadViewModel::onCallClick,
            onDeleteClick = dialpadViewModel::onDeleteClick,
            onAddContactClick = dialpadViewModel::onAddContactClick,
            onKeyClick = { dialpadViewModel.onKeyClick(it.toString()) },
        )
    }

    BottomSheet(visible = uiState.isKeypadOpen, onDismiss = callViewModel::onDismissKeypad) {
        Column {
            DialpadTextField(text = keypadUiState.value)
            Keypad(
                onKeyClick = {
                    callViewModel.onKeypadClick(it)
                    keypadViewModel.onValueClick(it.toString())
                }
            )
        }
    }

    BottomSheet(visible = uiState.isManageOpen, onDismiss = callViewModel::onDismissManage) {
        CallsList(
            items = uiState.calls,
            onHangupCallClick = { it.decline() },
            onSeparateCallClick = { try { it.leaveConference() } catch (_: Exception) {} }
        )
    }

    Call(
        callerName = uiState.callerData.name ?: stringResource(R.string.caller_unknown),
        stateText = uiState.call?.state?.stringRes?.let { stringResource(it) },
        elapsedSeconds = uiState.elapsedSeconds,
        callerImagePainter = callerPainter,
        callsState = uiState.callsState,
        visibleCallActions = uiState.visibleActions,
        enabledCallActions = uiState.enabledActions,
        activatedCallActions = uiState.activatedActions,
        onCallActionClick = callViewModel::onCallActionClick
    )
}
