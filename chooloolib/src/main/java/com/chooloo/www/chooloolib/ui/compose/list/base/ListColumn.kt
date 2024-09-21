package com.chooloo.www.chooloolib.ui.compose.list.base

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
        items(
            items = items,
            key = keyBuilder?.let { it::invoke } ?: {},
            itemContent = {
                val index = items.indexOf(it)
                val header = headersToIndex[index]

                header?.let {
                    this@LazyColumn.item(key = header) {
                        HeaderListItem(header = header)
                    }
                }
                itemBuilder(it)
            }
        )
    }
}
