package com.chooloo.www.chooloolib.ui.compose.list.item.preference

import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.chooloo.www.chooloolib.ui.compose.list.item.ListItem

@Composable
fun SwitchPreferenceListItem(
    modifier: Modifier = Modifier,
    title: String,
    value: Boolean?,
    enabled: Boolean = true,
    subtitle: String? = null,
    onValueChange: (Boolean) -> Unit,
    startContainer: @Composable (() -> Unit)? = null,
) {
    val isChecked = value ?: false

    ListItem(
        title = title,
        enabled = enabled,
        modifier = modifier,
        subtitle = subtitle,
        onClick = { onValueChange(!isChecked) },
        startContainer = startContainer,
        endContainer = {
            Switch(
                enabled = enabled,
                checked = isChecked,
                onCheckedChange = onValueChange
            )
        }
    )
}
