package com.chooloo.www.chooloolib.ui.view

import android.Manifest
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.chooloo.www.chooloolib.ui.compose.Dialpad
import com.chooloo.www.chooloolib.ui.compose.Permissioned
import com.chooloo.www.chooloolib.ui.viewmodel.dialpad.DialpadViewModelImpl

@Composable
fun DialerView(
    dialpadViewModel: DialpadViewModelImpl = hiltViewModel()
) {
    val uiState by dialpadViewModel.uiState.collectAsState()

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