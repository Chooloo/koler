package com.chooloo.www.chooloolib.ui.dialpad

import android.Manifest
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.chooloo.www.chooloolib.ui.components.Permissioned

@Composable
fun DialpadScreen(
    initialNumber: String? = null,
    dialpadViewModel: DialpadViewModel = hiltViewModel()
) {
    val uiState by dialpadViewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = initialNumber) {
        if (!initialNumber.isNullOrEmpty()) {
            dialpadViewModel.setNumber(initialNumber)
        }
    }

    Permissioned(
        permissions = listOf(Manifest.permission.CALL_PHONE)
    ) {
        Dialpad(
            value = uiState.value,
            onCallClick = dialpadViewModel::onCallClick,
            onDeleteClick = dialpadViewModel::onDeleteClick,
            onAddContactClick = dialpadViewModel::onAddContactClick,
            onDeleteLongClick = dialpadViewModel::onDeleteLongClick,
            onKeyClick = { dialpadViewModel.onKeyClick(it.toString()) },
        )
    }
}
