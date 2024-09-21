package com.chooloo.www.chooloolib.ui.compose

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.chooloo.www.chooloolib.ui.compose.list.item.ListItem

@Composable
fun MenuListItem(
    title: String, caption: String, imageVector: ImageVector, onClick: () -> Unit = {}
) {
    ListItem(
        title = title,
        onClick = onClick,
        subtitle = caption,
        startContainer = { Icon(imageVector, contentDescription = null) }
    )
}