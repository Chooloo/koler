package com.chooloo.www.chooloolib.ui.components

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable

@Composable
fun <Item> ListColumn(
    items: List<Item>,
    itemBuilder: @Composable (Item) -> Unit,
) {
    LazyColumn {
        items(
            items = items,
            itemContent = {
                itemBuilder.invoke(it)
            }
        )
    }
}