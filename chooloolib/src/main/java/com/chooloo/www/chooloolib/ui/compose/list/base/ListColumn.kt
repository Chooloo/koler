package com.chooloo.www.chooloolib.ui.compose.list.base

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
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
    val headersToIndex = mutableMapOf<Int, String>()
    headerBuilder?.let {
        var lastHeader: String? = null
        items.forEachIndexed { index, item ->
            val header = headerBuilder.invoke(item)
            if (header != lastHeader) {
                headersToIndex[index] = header
                lastHeader = header
            }
        }
    }

    LazyColumn(modifier = modifier) {
        items.mapIndexed { index, item ->
            val header = headersToIndex.getOrDefault(index, null)
            header?.let {
                item(key = header) {
                    HeaderListItem(header = header)
                }
            }

            item(key = keyBuilder?.invoke(item)) {
                itemBuilder(item)
            }
        }
    }
}
