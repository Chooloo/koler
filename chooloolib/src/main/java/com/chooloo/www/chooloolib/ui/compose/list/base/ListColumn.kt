package com.chooloo.www.chooloolib.ui.compose.list.base

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.chooloo.www.chooloolib.ui.compose.list.item.HeaderListItem

@Composable
fun <ItemType> ListColumn(
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
