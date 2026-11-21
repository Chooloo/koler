package com.chooloo.www.chooloolib.ui.recents

import android.Manifest
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.domain.model.Recent
import com.chooloo.www.chooloolib.domain.model.RecentGroup
import com.chooloo.www.chooloolib.ui.components.ConfirmDialog
import com.chooloo.www.chooloolib.ui.components.MessageToast
import com.chooloo.www.chooloolib.ui.components.Permissioned

@Composable
fun RecentsScreen(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    filter: String,
    onItemClick: (RecentGroup) -> Unit,
    viewModel: RecentsViewModel = hiltViewModel(),
) {
    Permissioned(
        permissions = listOf(Manifest.permission.READ_CALL_LOG),
        optionalPermissions = listOf(Manifest.permission.WRITE_CALL_LOG),
        rationaleRes = R.string.permission_rationale_call_log,
    ) {
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        var deleteIds by rememberSaveable { mutableStateOf<LongArray?>(null) }
        LaunchedEffect(key1 = filter) {
            viewModel.onFilterChanged(filter)
        }
        MessageToast(message = uiState.message, onShown = viewModel::onMessageShown)
        RecentsList(
            modifier = modifier,
            contentPadding = contentPadding,
            items = uiState.items,
            groupSimilar = uiState.isGroupRecentsEnabled && filter.isEmpty(),
            onItemClick = onItemClick,
            onDeleteClick = { deleteIds = it.recents.map(Recent::id).toLongArray() },
            loadingState = uiState.loadingState,
        )
        deleteIds?.let { ids ->
            ConfirmDialog(
                title = pluralStringResource(
                    R.plurals.confirm_delete_recents_title,
                    ids.size,
                    ids.size,
                ),
                text = stringResource(R.string.confirm_delete_recents),
                confirmLabel = stringResource(R.string.action_delete),
                onConfirm = { viewModel.onDeleteConfirmed(ids.toList()) },
                onDismiss = { deleteIds = null },
            )
        }
    }
}
