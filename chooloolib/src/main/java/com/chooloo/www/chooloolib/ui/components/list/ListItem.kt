package com.chooloo.www.chooloolib.ui.components.list

import androidx.compose.foundation.combinedClickable
import androidx.compose.material3.ListItem as MaterialListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow

@Composable
fun ListItem(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    onClick: (() -> Unit)? = null,
    onLongClick: (() -> Unit)? = null,
    startContainer: @Composable (() -> Unit)? = null,
    endContainer: @Composable (() -> Unit)? = null,
) {
    MaterialListItem(
        headlineContent = { Text(text = title, maxLines = 1, overflow = TextOverflow.Ellipsis) },
        supportingContent = subtitle?.let {
            { Text(text = it, maxLines = 1, overflow = TextOverflow.Ellipsis) }
        },
        leadingContent = startContainer,
        trailingContent = endContainer,
        colors = ListItemDefaults.colors(containerColor = Color.Transparent),
        modifier = Modifier
            .clip(MaterialTheme.shapes.extraLarge)
            .then(modifier)
            .then(
                if (onClick != null) {
                    Modifier.combinedClickable(
                        role = Role.Button,
                        onClick = onClick,
                        onLongClick = onLongClick,
                    )
                } else {
                    Modifier
                },
            ),
    )
}
