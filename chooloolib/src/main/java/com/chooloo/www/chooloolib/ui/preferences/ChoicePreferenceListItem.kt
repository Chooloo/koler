package com.chooloo.www.chooloolib.ui.preferences

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.chooloo.www.chooloolib.ui.components.BottomSheet
import com.chooloo.www.chooloolib.ui.components.list.List
import com.chooloo.www.chooloolib.ui.components.list.ListItem

@Composable
fun <T> ChoicePreferenceListItem(
    title: String,
    values: List<T>,
    selectedValue: T,
    label: @Composable (T) -> String,
    onValueSelected: (T) -> Unit,
) {
    var isChoosing by remember { mutableStateOf(false) }

    ListItem(title = title, subtitle = label(selectedValue), onClick = { isChoosing = true })

    BottomSheet(visible = isChoosing, onDismiss = { isChoosing = false }) {
        List(
            items = values,
            item = { value ->
                ListItem(
                    title = label(value),
                    selected = value == selectedValue,
                    onClick = {
                        onValueSelected(value)
                        isChoosing = false
                    }
                )
            }
        )
    }
}
