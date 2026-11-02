package com.chooloo.www.chooloolib.ui.caller

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Call
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.domain.model.Phone
import com.chooloo.www.chooloolib.ui.components.list.List
import com.chooloo.www.chooloolib.utils.LoadingState

@Composable
fun PhonesList(
    modifier: Modifier = Modifier,
    items: List<Phone>,
    onItemClick: (item: Phone) -> Unit = {},
    onItemLongClick: (item: Phone) -> Unit = {},
    onItemActionClick: (item: Phone) -> Unit = {},
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
                phone = item,
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
            Phone(id = 0, contactId = 0, number = "123123"),
        ),
        loadingState = LoadingState.SUCCESS
    )
}

@Preview
@Composable
fun LoadingPhonesListPreview() {
    PhonesList(
        items = listOf(
            Phone(id = 0, contactId = 0, number = "123123"),
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