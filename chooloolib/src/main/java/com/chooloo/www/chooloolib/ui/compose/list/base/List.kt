package com.chooloo.www.chooloolib.ui.compose.list.base

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.domain.model.record.ContactRecord
import com.chooloo.www.chooloolib.ui.compose.Empty
import com.chooloo.www.chooloolib.ui.compose.Loading
import com.chooloo.www.chooloolib.ui.compose.list.item.ListItem
import com.chooloo.www.chooloolib.utils.LoadingState

@Composable
fun <ItemType> List(
    modifier: Modifier = Modifier,
    items: List<ItemType>,
    @StringRes emptyTitle: Int? = null,
    @StringRes loadingTitle: Int? = null,
    loadingState: LoadingState = LoadingState.IDLE,
    key: ((ItemType) -> String)? = null,
    header: ((ItemType) -> String)? = null,
    item: @Composable (ItemType) -> Unit,
    emptyImage: @Composable (() -> Unit)? = null,
) {
    if (loadingState == LoadingState.LOADING) {
        Loading(
            modifier = modifier,
            titleRes = loadingTitle
        )
    } else if (items.isNotEmpty()) {
        ListColumn(
            items = items,
            keyBuilder = key,
            itemBuilder = item,
            modifier = modifier,
            headerBuilder = header
        )
    } else {
        Empty(
            modifier = modifier,
            titleRes = emptyTitle,
            image = emptyImage
        )
    }
}

@Preview
@Composable
fun ListPreview() {
    List(
        items = listOf(ContactRecord(0, "Nahum Test", starred = false), ContactRecord()),
        loadingState = LoadingState.SUCCESS,
        emptyImage = { Icons.Default.Person },
        emptyTitle = R.string.no_results_contacts,
        item = { contact ->
            ListItem(
                title = contact.name ?: "Unknown", subtitle = contact.starred.toString()
            )
        })
}

@Preview
@Composable
fun LoadingListPreview() {
    List(
        items = listOf(ContactRecord(0, "Nahum Test", starred = false), ContactRecord()),
        loadingState = LoadingState.LOADING,
        emptyImage = { Icons.Default.Person },
        emptyTitle = R.string.no_results_contacts,
        item = { contact ->
            ListItem(
                title = contact.name ?: "Unknown", subtitle = contact.starred.toString()
            )
        })
}

@Preview
@Composable
fun EmptyListPreview() {
    List(
        items = emptyList<ContactRecord>(),
        loadingState = LoadingState.IDLE,
        emptyImage = { Icons.Default.Person },
        emptyTitle = R.string.no_results_contacts,
        item = { contact ->
            ListItem(
                title = contact.name ?: "Unknown", subtitle = contact.starred.toString()
            )
        })
}