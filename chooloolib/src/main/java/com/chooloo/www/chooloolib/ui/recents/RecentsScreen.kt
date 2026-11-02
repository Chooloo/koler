package com.chooloo.www.chooloolib.ui.recents

import android.Manifest
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.chooloo.www.chooloolib.domain.model.RecentGroup
import com.chooloo.www.chooloolib.ui.components.Permissioned

@Composable
fun RecentsScreen(
    modifier: Modifier = Modifier,
    filter: String? = null,
    onItemClick: (RecentGroup) -> Unit = {},
    onItemLongClick: (RecentGroup) -> Unit = {},
    viewModel: RecentsViewModel = hiltViewModel()
) {
    Permissioned(
        permissions = listOf(
            Manifest.permission.READ_CALL_LOG,
            Manifest.permission.WRITE_CALL_LOG
        )
    ) {
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        val isGroupRecentsEnabled by viewModel.isGroupRecentsEnabled.collectAsStateWithLifecycle()
        LaunchedEffect(key1 = filter) {
            viewModel.onFilterChanged(filter)
        }
        RecentsList(
            modifier = modifier,
            items = uiState.items,
            groupSimilar = isGroupRecentsEnabled && filter.isNullOrEmpty(),
            onItemClick = onItemClick,
            onItemLongClick = onItemLongClick,
            loadingState = uiState.loadingState,
        )
    }
}
