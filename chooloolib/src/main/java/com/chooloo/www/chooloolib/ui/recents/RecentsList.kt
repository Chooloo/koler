package com.chooloo.www.chooloolib.ui.recents

import android.text.format.DateUtils
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.History
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.domain.model.Recent
import com.chooloo.www.chooloolib.domain.model.RecentGroup
import com.chooloo.www.chooloolib.ui.common.LoadingState
import com.chooloo.www.chooloolib.ui.components.list.ItemList

@Composable
fun RecentsList(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    items: List<Recent>,
    groupSimilar: Boolean = false,
    onItemClick: (item: RecentGroup) -> Unit,
    onItemLongClick: ((RecentGroup) -> Unit)?,
    loadingState: LoadingState,
) {
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
            RecentListItem(
                group = group,
                onClick = { onItemClick(group) },
                onLongClick = onItemLongClick?.let { { it(group) } },
            )
        },
    )
}
