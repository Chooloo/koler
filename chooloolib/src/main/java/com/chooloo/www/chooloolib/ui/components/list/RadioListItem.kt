package com.chooloo.www.chooloolib.ui.components.list

import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.RadioButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role

@Composable
fun RadioListItem(title: String, isSelected: Boolean, onClick: () -> Unit) {
    ListItem(
        title = title,
        modifier = Modifier.selectable(
            selected = isSelected,
            role = Role.RadioButton,
            onClick = onClick,
        ),
        startContainer = { RadioButton(selected = isSelected, onClick = null) },
    )
}
