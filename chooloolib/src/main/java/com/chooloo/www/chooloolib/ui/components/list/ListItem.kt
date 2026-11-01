package com.chooloo.www.chooloolib.ui.components.list

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Call
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import com.chooloo.www.chooloolib.ui.theme.Spacing

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ListItem(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String? = null,
    enabled: Boolean = true,
    selected: Boolean = false,
    clickable: Boolean = true,
    highlightText: String? = null,
    onClick: () -> Unit = {},
    onLongClick: () -> Unit = {},
    endContainer: @Composable (() -> Unit)? = null,
    startContainer: @Composable (() -> Unit)? = null,
    extraSubtitleContainer: @Composable (() -> Unit)? = null,
) {
    val annotatedTitle: AnnotatedString = buildAnnotatedString {
        append(title)
        if (!highlightText.isNullOrEmpty()) {
            val startIndex = title.indexOf(highlightText, ignoreCase = true)
            if (startIndex >= 0) {
                addStyle(
                    start = startIndex,
                    end = startIndex + highlightText.length,
                    style = SpanStyle(color = MaterialTheme.colorScheme.primary),
                )
            }
        }
    }

    Card(
        colors = CardDefaults.cardColors(
            containerColor = if (selected) MaterialTheme.colorScheme.secondaryContainer else Color.Transparent
        ),
        shape = MaterialTheme.shapes.large,
        modifier = modifier
            .combinedClickable(
                enabled = clickable && enabled,
                onClick = onClick::invoke,
                onLongClick = onLongClick::invoke
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    vertical = Spacing.Small,
                    horizontal = Spacing.Medium
                ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            startContainer?.let { it() }
            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    style = typography.titleMedium,
                    text = annotatedTitle
                )
                subtitle?.let {
                    Row {
                        Text(
                            text = subtitle,
                            style = typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        extraSubtitleContainer?.let { it() }
                    }
                }
            }
            endContainer?.let {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    it()
                }
            }
        }
    }
}

@Preview
@Composable
private fun ListItemPreview() {
    MaterialTheme {
        ListItem(
            title = "This is title",
            subtitle = "This is subtitle",
            startContainer = {
                Icons.Rounded.Call
            },
            endContainer = {
                Switch(checked = false, onCheckedChange = {})
            }
        )
    }
}
