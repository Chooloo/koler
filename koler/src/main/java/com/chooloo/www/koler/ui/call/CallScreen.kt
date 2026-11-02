package com.chooloo.www.koler.ui.call

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.rememberAsyncImagePainter
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.ui.common.createContact
import com.chooloo.www.chooloolib.ui.components.BottomSheet
import com.chooloo.www.chooloolib.ui.components.DialpadTextField
import com.chooloo.www.chooloolib.ui.components.Keypad
import com.chooloo.www.chooloolib.ui.components.list.List
import com.chooloo.www.chooloolib.ui.components.list.ListItem
import com.chooloo.www.chooloolib.ui.dialpad.Dialpad
import com.chooloo.www.chooloolib.ui.dialpad.DialpadViewModel
import com.chooloo.www.koler.ui.call.CallViewModel.Sheet

@Composable
fun CallScreen(
    callViewModel: CallViewModel = hiltViewModel(),
    dialpadViewModel: DialpadViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val uiState by callViewModel.uiState.collectAsStateWithLifecycle()
    val dialpadUiState by dialpadViewModel.uiState.collectAsStateWithLifecycle()
    val call = uiState.call
    val caller = uiState.caller

    val defaultPainter = rememberVectorPainter(Icons.Rounded.Person)
    val callerPainter = if (caller?.photoUri != null) {
        rememberAsyncImagePainter(model = caller.photoUri, error = defaultPainter, placeholder = defaultPainter)
    } else {
        defaultPainter
    }

    BottomSheet(visible = uiState.sheet == Sheet.ADD_CALL, onDismiss = callViewModel::onSheetDismiss) {
        Dialpad(
            value = dialpadUiState.value,
            onCallClick = dialpadViewModel::onCallClick,
            onDeleteClick = dialpadViewModel::onDeleteClick,
            onAddContactClick = { context.createContact(dialpadUiState.value) },
            onKeyClick = dialpadViewModel::onKeyClick,
        )
    }

    BottomSheet(visible = uiState.sheet == Sheet.KEYPAD, onDismiss = callViewModel::onSheetDismiss) {
        Column {
            DialpadTextField(text = uiState.keypadValue)
            Keypad(onKeyClick = callViewModel::onKeypadKeyClick)
        }
    }

    BottomSheet(visible = uiState.sheet == Sheet.MANAGE, onDismiss = callViewModel::onSheetDismiss) {
        CallsList(
            items = uiState.calls,
            onHangupCallClick = callViewModel::onHangupCall,
            onSeparateCallClick = callViewModel::onSeparateCall,
        )
    }

    BottomSheet(visible = uiState.sheet == Sheet.AUDIO_ROUTE, onDismiss = callViewModel::onSheetDismiss) {
        List(
            items = uiState.audio.supportedRoutes,
            key = { it.name },
            item = { route ->
                ListItem(
                    title = stringResource(route.labelRes),
                    selected = route == uiState.audio.route,
                    onClick = { callViewModel.onAudioRouteSelected(route) }
                )
            }
        )
    }

    CallContent(
        callerName = when {
            call?.isConference == true -> stringResource(R.string.conference)
            else -> caller?.name ?: call?.displayName ?: call?.number ?: stringResource(R.string.caller_unknown)
        },
        stateText = call?.state?.let { stringResource(it.labelRes) },
        elapsedSeconds = uiState.elapsedSeconds,
        callerImagePainter = callerPainter,
        callsState = uiState.callsState,
        visibleCallActions = uiState.visibleActions,
        enabledCallActions = uiState.enabledActions,
        activatedCallActions = uiState.activatedActions,
        onCallActionClick = callViewModel::onCallActionClick
    )
}
