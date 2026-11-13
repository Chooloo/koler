package com.chooloo.www.koler.ui.call

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.ui.components.BottomSheet
import com.chooloo.www.chooloolib.ui.components.DialpadNumber
import com.chooloo.www.chooloolib.ui.components.Keypad
import com.chooloo.www.chooloolib.ui.components.list.ItemList
import com.chooloo.www.chooloolib.ui.components.list.ListItem
import com.chooloo.www.chooloolib.ui.components.list.RadioListItem
import com.chooloo.www.chooloolib.ui.dialpad.DialpadScreen
import com.chooloo.www.koler.domain.enum.CallsState
import com.chooloo.www.koler.ui.call.CallViewModel.Sheet
import com.chooloo.www.koler.ui.common.CallLabels

@Composable
fun CallScreen(callViewModel: CallViewModel = hiltViewModel()) {
    val context = LocalContext.current
    val uiState by callViewModel.uiState.collectAsStateWithLifecycle()
    val call = uiState.call
    val caller = uiState.caller

    BottomSheet(
        visible = uiState.sheet == Sheet.ADD_CALL,
        skipPartiallyExpanded = true,
        onDismiss = callViewModel::onSheetDismiss,
    ) {
        DialpadScreen()
    }

    BottomSheet(
        visible = uiState.sheet == Sheet.KEYPAD,
        skipPartiallyExpanded = true,
        onDismiss = callViewModel::onSheetDismiss,
    ) {
        Column {
            DialpadNumber(number = uiState.keypadValue)
            Keypad(onKeyClick = callViewModel::onKeypadKeyClick)
        }
    }

    BottomSheet(
        visible = uiState.sheet == Sheet.MANAGE,
        onDismiss = callViewModel::onSheetDismiss,
    ) {
        CallsList(
            items = uiState.calls,
            onHangupCallClick = callViewModel::onHangupCall,
            onSeparateCallClick = callViewModel::onSeparateCall,
        )
    }

    BottomSheet(
        visible = uiState.sheet == Sheet.AUDIO_ROUTE,
        onDismiss = callViewModel::onSheetDismiss,
    ) {
        ItemList(
            items = uiState.audio.supportedRoutes,
            key = { it.name },
            item = { route ->
                RadioListItem(
                    title = stringResource(CallLabels.labelRes(route)),
                    isSelected = route == uiState.audio.route,
                ) {
                    callViewModel.onAudioRouteSelected(route)
                }
            },
        )
    }

    BottomSheet(
        visible = uiState.callsState == CallsState.WAITING_FOR_ACCOUNT,
        onDismiss = callViewModel::onPhoneAccountDismiss,
    ) {
        ItemList(
            items = call?.phoneAccounts.orEmpty(),
            key = { it.id },
            item = { account ->
                ListItem(
                    title = account.label,
                    onClick = { callViewModel.onPhoneAccountSelected(account) },
                )
            },
        )
    }

    CallContent(
        callerName = call?.let { CallLabels.title(it, context, caller) }
            ?: stringResource(R.string.caller_unknown),
        status = callStatus(
            uiState.elapsedSeconds,
            call?.state?.let { stringResource(CallLabels.labelRes(it)) },
        ),
        avatarName = caller?.name?.takeUnless { call?.isConference == true },
        callerPhotoUri = caller?.photoUri,
        callsState = uiState.callsState,
        actions = uiState.actions,
        onActionClick = callViewModel::onCallActionClick,
        onAccept = callViewModel::onAccept,
        onHangup = callViewModel::onHangup,
        onManage = callViewModel::onManage,
    )
}
