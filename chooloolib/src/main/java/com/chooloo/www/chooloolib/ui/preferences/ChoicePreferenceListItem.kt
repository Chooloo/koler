package com.chooloo.www.chooloolib.ui.preferences

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.ui.components.list.ListItem
import com.chooloo.www.chooloolib.ui.components.list.RadioListItem

@Composable
fun <T> ChoicePreferenceListItem(
    icon: ImageVector,
    title: String,
    values: List<T>,
    selectedValue: T,
    label: @Composable (T) -> String,
    onValueSelected: (T) -> Unit,
) {
    var isChoosing by remember { mutableStateOf(false) }

    ListItem(
        title = title,
        subtitle = label(selectedValue),
        onClick = { isChoosing = true },
        startContainer = { PreferenceIcon(icon) },
    )

    if (isChoosing) {
        AlertDialog(
            onDismissRequest = { isChoosing = false },
            title = { Text(title) },
            text = {
                Column {
                    values.forEach { value ->
                        RadioListItem(title = label(value), isSelected = value == selectedValue) {
                            onValueSelected(value)
                            isChoosing = false
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { isChoosing = false }) {
                    Text(stringResource(R.string.action_cancel))
                }
            },
        )
    }
}
