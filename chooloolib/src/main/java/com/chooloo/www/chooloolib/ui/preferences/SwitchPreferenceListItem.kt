package com.chooloo.www.chooloolib.ui.preferences

import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import com.chooloo.www.chooloolib.ui.components.list.ListItem

@Composable
fun SwitchPreferenceListItem(
    title: String,
    value: Boolean,
    onValueChange: (Boolean) -> Unit,
) {
    ListItem(
        title = title,
        onClick = { onValueChange(!value) },
        endContainer = { Switch(checked = value, onCheckedChange = onValueChange) }
    )
}
