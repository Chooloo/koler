package com.chooloo.www.chooloolib.ui.compose.list.item.preference

import androidx.compose.material3.Checkbox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.chooloo.www.chooloolib.ui.compose.list.item.ListItem

@Composable
fun CheckboxPreferenceListItem(
    modifier: Modifier = Modifier,
    title: String,
    value: Boolean,
    enabled: Boolean = true,
    subtitle: String? = null,
    onValueChange: (Boolean) -> Unit,
    startContainer: @Composable (() -> Unit)? = null,
) {
    ListItem(
        title = title,
        modifier = modifier,
        subtitle = subtitle,
        startContainer = startContainer,
        endContainer = {
            Checkbox(
                checked = value,
                enabled = enabled,
                onCheckedChange = onValueChange
            )
        }
    )
}