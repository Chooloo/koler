package com.chooloo.www.chooloolib.ui.recents

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.History
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.domain.model.RecentData
import com.chooloo.www.chooloolib.ui.components.list.List
import com.chooloo.www.chooloolib.utils.LoadingState
import com.chooloo.www.chooloolib.utils.getRelativeDateString
import java.util.Date

@Composable
fun RecentsList(
    modifier: Modifier = Modifier,
    items: List<RecentData>,
    groupSimilar: Boolean = false,
    onItemClick: (item: RecentData) -> Unit = {},
    onItemLongClick: (item: RecentData) -> Unit = {},
    loadingState: LoadingState = LoadingState.IDLE,
) {
    List(
        modifier = modifier,
        loadingState = loadingState,
        emptyImage = {
            Icon(
                Icons.Rounded.History,
                stringResource(id = R.string.cd_history)
            )
        },
        loadingTitle = R.string.loading_recents,
        emptyTitle = R.string.no_results_recents,
        items = if (groupSimilar) RecentData.group(items) else items,
        key = { item -> item.id.toString() },
        header = { item -> getRelativeDateString(item.date) },
        item = { item ->
            RecentListItem(
                recentData = item,
                onClick = { onItemClick(item) },
                onLongClick = { onItemLongClick(item) }
            )
        }
    )
}

@Preview
@Composable
fun RecentsListPreview() {
    RecentsList(
        items = listOf(
            RecentData(
                id = 0,
                number = "1234",
                date = Date(1234),
            ),
            RecentData(
                id = 1,
                number = "1234",
                date = Date(1234),
            ),
        ),
        loadingState = LoadingState.SUCCESS
    )
}

@Preview
@Composable
fun LoadingRecentsListPreview() {
    RecentsList(
        items = listOf(
            RecentData(
                id = 0,
                number = "1234",
                date = Date(1234),
            ),
            RecentData(
                id = 1,
                number = "1234",
                date = Date(1234),
            ),
        ),
        loadingState = LoadingState.LOADING
    )
}

@Preview
@Composable
fun EmptyRecentsListPreview() {
    RecentsList(
        items = emptyList(), loadingState = LoadingState.IDLE
    )
}
