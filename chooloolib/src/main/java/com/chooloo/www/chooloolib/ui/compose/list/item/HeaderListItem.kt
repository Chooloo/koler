package com.chooloo.www.chooloolib.ui.compose.list.item

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

fun LazyListScope.header(header: String) {
    item(key = header) {
        HeaderListItem(header = header  )
    }
}

@Composable
fun HeaderListItem(header: String) {
    Text(text = header)
}