package com.chooloo.www.chooloolib.ui.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.chooloo.www.chooloolib.ui.compose.BottomSheet
import com.chooloo.www.chooloolib.ui.compose.Call
import com.chooloo.www.chooloolib.ui.compose.Dialpad
import com.chooloo.www.chooloolib.ui.compose.DialpadTextField
import com.chooloo.www.chooloolib.ui.compose.Keypad
import com.chooloo.www.chooloolib.ui.viewmodel.call.CallViewModelImpl
import com.chooloo.www.chooloolib.ui.viewmodel.dialpad.DialpadViewModelImpl
import com.chooloo.www.chooloolib.ui.viewmodel.keypad.KeypadViewModelImpl

@Composable
fun CallView(
    callViewModel: CallViewModelImpl = hiltViewModel(),
    keypadViewModel: KeypadViewModelImpl = hiltViewModel(),
    dialpadViewModel: DialpadViewModelImpl = hiltViewModel()
) {
    val uiState by callViewModel.uiState.collectAsState()
    val keypadUiState by keypadViewModel.uiState.collectAsState()
    val dialpadUiState by dialpadViewModel.uiState.collectAsState()

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
        DialpadTextField(text = keypadUiState.value)
        Keypad(
            onKeyClick = {
                callViewModel.onKeypadClick(it)
                keypadViewModel.onValueClick(it.toString())
            }
        )
    }

    Call(
        callerName = "",
        callsState = uiState.callsState,
        visibleCallActions = uiState.visibleActions,
        onCallActionClick = callViewModel::onCallActionClick
    )
}