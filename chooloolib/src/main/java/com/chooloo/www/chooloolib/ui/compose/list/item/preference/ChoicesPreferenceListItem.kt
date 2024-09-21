package com.chooloo.www.chooloolib.ui.compose.list.item.preference

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.chooloo.www.chooloolib.ui.compose.BottomSheet
import com.chooloo.www.chooloolib.ui.compose.list.base.List
import com.chooloo.www.chooloolib.ui.compose.list.item.ListItem
import com.chooloo.www.chooloolib.utils.LoadingState

@Composable
fun <ItemType> ChoicesPreferenceListItem(
    title: String,
    values: List<ItemType>,
    enabled: Boolean = true,
    subtitle: String? = null,
    selectedValues: List<ItemType>,
    loadingState: LoadingState = LoadingState.IDLE,
    onValueChanged: (item: ItemType, selected: Boolean) -> Unit = { _, _ -> },
    item: @Composable (item: ItemType, selected: Boolean, onClick: () -> Unit) -> Unit
) {
    val choicesVisible = remember { mutableStateOf(false) }

    ListItem(
        title = title,
        enabled = enabled,
        subtitle = subtitle,
        onClick = { choicesVisible.value = true }
    )

    BottomSheet(
        visible = choicesVisible.value,
        onDismiss = { choicesVisible.value = false }
    ) {
        List(
            items = values,
            loadingState = loadingState,
            item = {
                item(
                    item = it,
                    selected = it in selectedValues,
                    onClick = { onValueChanged(it, it !in selectedValues) }
                )
            }
        )
    }
}