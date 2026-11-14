package com.chooloo.www.chooloolib.ui.dialpad

import android.Manifest
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.ui.common.rememberExternalActions
import com.chooloo.www.chooloolib.ui.components.MessageToast
import com.chooloo.www.chooloolib.ui.components.Permissioned

@Composable
fun DialpadScreen(
    initialNumber: String? = null,
    dialpadViewModel: DialpadViewModel = hiltViewModel(),
) {
    val externalActions = rememberExternalActions()
    val uiState by dialpadViewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = initialNumber) {
        if (!initialNumber.isNullOrEmpty()) {
            dialpadViewModel.setNumber(initialNumber)
        }
    }

    MessageToast(message = uiState.message, onShown = dialpadViewModel::onMessageShown)

    Permissioned(
        permissions = listOf(Manifest.permission.CALL_PHONE),
        rationaleRes = R.string.permission_rationale_phone,
    ) {
        Dialpad(
            value = uiState.value,
            onCallClick = dialpadViewModel::onCallClick,
            onDeleteClick = dialpadViewModel::onDeleteClick,
            onAddContactClick = { externalActions.createContact(uiState.value) },
            onDeleteLongClick = dialpadViewModel::onDeleteLongClick,
            onKeyClick = dialpadViewModel::onKeyClick,
            onKeyLongClick = dialpadViewModel::onKeyLongClick,
        )
    }
}
