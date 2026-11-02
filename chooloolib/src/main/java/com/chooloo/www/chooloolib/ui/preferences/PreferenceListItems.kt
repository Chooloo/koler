package com.chooloo.www.chooloolib.ui.preferences

import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import com.chooloo.www.chooloolib.ui.components.list.ListItem

@Composable
fun ActionPreferenceListItem(icon: ImageVector, title: String, subtitle: String?, onClick: () -> Unit) {
    ListItem(title = title, subtitle = subtitle, onClick = onClick, startContainer = { PreferenceIcon(icon) })
}

@Composable
fun SwitchPreferenceListItem(icon: ImageVector, title: String, value: Boolean, onValueChange: (Boolean) -> Unit) {
    ListItem(
        title = title,
        modifier = Modifier.toggleable(value = value, role = Role.Switch, onValueChange = onValueChange),
        startContainer = { PreferenceIcon(icon) },
        endContainer = { Switch(checked = value, onCheckedChange = null) }
    )
}

@Composable
fun PreferenceIcon(icon: ImageVector) {
    Icon(
        imageVector = icon,
        contentDescription = null,
        tint = MaterialTheme.colorScheme.onSurfaceVariant,
    )
}
