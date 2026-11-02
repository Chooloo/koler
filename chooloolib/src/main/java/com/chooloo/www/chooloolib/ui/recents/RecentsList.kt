package com.chooloo.www.chooloolib.ui.recents

import android.text.format.DateUtils
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.History
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.domain.model.Recent
import com.chooloo.www.chooloolib.domain.model.RecentGroup
import com.chooloo.www.chooloolib.domain.model.groupedByNumberAndDay
import com.chooloo.www.chooloolib.domain.model.ungrouped
import com.chooloo.www.chooloolib.ui.common.LoadingState
import com.chooloo.www.chooloolib.ui.components.list.List

@Composable
fun RecentsList(
    modifier: Modifier = Modifier,
    items: List<Recent>,
    groupSimilar: Boolean = false,
    onItemClick: (item: RecentGroup) -> Unit = {},
    onItemLongClick: (item: RecentGroup) -> Unit = {},
    loadingState: LoadingState = LoadingState.SUCCESS,
) {
    val groups = remember(items, groupSimilar) {
        if (groupSimilar) items.groupedByNumberAndDay() else items.ungrouped()
    }

    List(
        modifier = modifier,
        loadingState = loadingState,
        emptyImage = {
            Icon(Icons.Rounded.History, stringResource(id = R.string.cd_history))
        },
        loadingTitle = R.string.loading_recents,
        emptyTitle = R.string.no_results_recents,
        items = groups,
        key = { group -> group.latest.id.toString() },
        header = { group ->
            DateUtils.getRelativeTimeSpanString(
                group.latest.dateMillis,
                System.currentTimeMillis(),
                DateUtils.DAY_IN_MILLIS
            ).toString()
        },
        item = { group ->
            RecentListItem(
                group = group,
                onClick = { onItemClick(group) },
                onLongClick = { onItemLongClick(group) }
            )
        }
    )
}

private val previewRecents = listOf(
    Recent(id = 0, number = "1234", dateMillis = 1234, type = Recent.Type.INCOMING),
    Recent(id = 1, number = "1234", dateMillis = 1234, type = Recent.Type.MISSED),
)

@Preview
@Composable
fun RecentsListPreview() {
    RecentsList(items = previewRecents, loadingState = LoadingState.SUCCESS)
}
