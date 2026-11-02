package com.chooloo.www.chooloolib.ui.components.list

import androidx.annotation.StringRes
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.domain.model.Contact
import com.chooloo.www.chooloolib.ui.common.LoadingState
import com.chooloo.www.chooloolib.ui.components.Empty
import com.chooloo.www.chooloolib.ui.components.Loading

@Composable
fun <ItemType> List(
    modifier: Modifier = Modifier,
    items: List<ItemType>,
    @StringRes emptyTitle: Int? = null,
    @StringRes loadingTitle: Int? = null,
    loadingState: LoadingState = LoadingState.SUCCESS,
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

@Composable
private fun <ItemType> ListColumn(
    modifier: Modifier = Modifier,
    items: List<ItemType>,
    keyBuilder: ((ItemType) -> String)? = null,
    itemBuilder: @Composable (ItemType) -> Unit,
    headerBuilder: ((ItemType) -> String)? = null,
) {
    val headersToIndex = remember(items, headerBuilder) {
        buildMap {
            headerBuilder?.let { builder ->
                var lastHeader: String? = null
                items.forEachIndexed { index, item ->
                    val header = builder(item)
                    if (header != lastHeader) {
                        put(index, header)
                        lastHeader = header
                    }
                }
            }
        }
    }

    LazyColumn(modifier = modifier) {
        itemsIndexed(
            items = items,
            key = keyBuilder?.let { builder -> { index, item -> "${builder(item)}_$index" } }
        ) { index, item ->
            headersToIndex[index]?.let { header ->
                HeaderListItem(header = header)
            }
            itemBuilder(item)
        }
    }
}

@Preview
@Composable
fun ListPreview() {
    List(
        items = listOf(Contact(0, "Nahum Test"), Contact(1, "Test")),
        loadingState = LoadingState.SUCCESS,
        emptyImage = { Icons.Default.Person },
        emptyTitle = R.string.no_results_contacts,
        item = { contact ->
            ListItem(
                title = contact.name ?: "Unknown", subtitle = contact.isFavorite.toString()
            )
        })
}
