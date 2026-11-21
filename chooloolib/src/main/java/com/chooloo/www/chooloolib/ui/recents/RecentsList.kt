package com.chooloo.www.chooloolib.ui.recents

import android.text.format.DateUtils
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.History
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.domain.model.Recent
import com.chooloo.www.chooloolib.domain.model.RecentGroup
import com.chooloo.www.chooloolib.ui.common.LoadingState
import com.chooloo.www.chooloolib.ui.common.rememberExternalActions
import com.chooloo.www.chooloolib.ui.components.list.ItemList

@Composable
fun RecentsList(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    items: List<Recent>,
    groupSimilar: Boolean = false,
    onItemClick: (item: RecentGroup) -> Unit,
    onDeleteClick: (RecentGroup) -> Unit,
    loadingState: LoadingState,
) {
    val externalActions = rememberExternalActions()
    var menuRecentId by remember { mutableStateOf<Long?>(null) }
    val groups = remember(items, groupSimilar) {
        if (groupSimilar) {
            RecentGroup.groupedByNumberAndDay(items)
        } else {
            RecentGroup.ungrouped(items)
        }
    }

    ItemList(
        modifier = modifier,
        contentPadding = contentPadding,
        loadingState = loadingState,
        emptyIcon = Icons.Rounded.History,
        loadingTitle = R.string.loading_recents,
        emptyTitle = R.string.no_results_recents,
        items = groups,
        key = { group -> group.latest.id.toString() },
        header = { group ->
            DateUtils.getRelativeTimeSpanString(
                group.latest.dateMillis,
                System.currentTimeMillis(),
                DateUtils.DAY_IN_MILLIS,
            ).toString()
        },
        item = { group ->
            Box {
                RecentListItem(
                    group = group,
                    onClick = { onItemClick(group) },
                    onLongClick = { menuRecentId = group.latest.id },
                )
                DropdownMenu(
                    expanded = group.latest.id == menuRecentId,
                    onDismissRequest = { menuRecentId = null },
                ) {
                    if (group.latest.number.isNotEmpty()) {
                        DropdownMenuItem(
                            text = { Text(stringResource(R.string.action_copy_number)) },
                            onClick = {
                                menuRecentId = null
                                externalActions.copyToClipboard(group.latest.number)
                            },
                        )
                    }
                    DropdownMenuItem(
                        text = { Text(stringResource(R.string.action_delete)) },
                        onClick = {
                            menuRecentId = null
                            onDeleteClick(group)
                        },
                    )
                }
            }
        },
    )
}
