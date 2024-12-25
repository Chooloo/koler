package com.chooloo.www.chooloolib.ui.compose.list

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Call
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.domain.model.PhoneData
import com.chooloo.www.chooloolib.ui.compose.list.base.List
import com.chooloo.www.chooloolib.ui.compose.list.item.PhoneListItem
import com.chooloo.www.chooloolib.utils.LoadingState

@Composable
fun PhonesList(
    modifier: Modifier = Modifier,
    items: List<PhoneData>,
    onItemClick: (item: PhoneData) -> Unit = {},
    onItemLongClick: (item: PhoneData) -> Unit = {},
    onItemActionClick: (item: PhoneData) -> Unit = {},
    loadingState: LoadingState = LoadingState.IDLE,
) {
    List(
        modifier = modifier,
        items = items.distinctBy { it.number },
        loadingState = loadingState,
        loadingTitle = R.string.loading_phones,
        emptyTitle = R.string.no_results_phones,
        emptyImage = {
            Icon(
                Icons.Rounded.Call,
                stringResource(id = R.string.cd_call)
            )
        },
        key = { item -> item.hashCode().toString() },
        item = { item ->
            PhoneListItem(
                phoneItem = item,
                onClick = { onItemClick(item) },
                onLongClick = { onItemLongClick(item) },
                onOpenClick = { onItemActionClick(item) }
            )
        }
    )
}

@Preview
@Composable
fun PhonesListPreview() {
    PhonesList(
        items = listOf(
            PhoneData(
                number = "123123",
                normalizedNumber = "123123"
            ),
        ),
        loadingState = LoadingState.SUCCESS
    )
}

@Preview
@Composable
fun LoadingPhonesListPreview() {
    PhonesList(
        items = listOf(
            PhoneData(
                number = "123123",
                normalizedNumber = "123123"
            ),
        ),
        loadingState = LoadingState.LOADING
    )
}

@Preview
@Composable
fun EmptyPhonesListPreview() {
    PhonesList(
        items = emptyList(),
        loadingState = LoadingState.IDLE,
    )
}