package com.chooloo.www.chooloolib.ui.view

import android.Manifest
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.chooloo.www.chooloolib.domain.model.RecentData
import com.chooloo.www.chooloolib.ui.compose.Permissioned
import com.chooloo.www.chooloolib.ui.compose.list.RecentsList
import com.chooloo.www.chooloolib.ui.viewmodel.recents.RecentsViewModelImpl

@Composable
fun RecentsView(
    modifier: Modifier = Modifier,
    filter: String? = null,
    onItemClick: (RecentData) -> Unit = {},
    onItemLongClick: (RecentData) -> Unit = {},
    viewModel: RecentsViewModelImpl = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(key1 = filter) {
        viewModel.onFilterChanged(filter)
    }

    Permissioned(
        permissions = listOf(
            Manifest.permission.READ_CALL_LOG,
            Manifest.permission.WRITE_CALL_LOG
        )
    ) {
        RecentsList(
            modifier = modifier,
            items = uiState.items,
            onItemClick = onItemClick,
            onItemLongClick = onItemLongClick,
            loadingState = uiState.loadingState,
        )
    }
}