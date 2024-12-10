package com.chooloo.www.chooloolib.ui.compose.list.item.preference

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.chooloo.www.chooloolib.ui.compose.BottomSheet
import com.chooloo.www.chooloolib.ui.compose.list.base.List
import com.chooloo.www.chooloolib.ui.compose.list.item.ListItem
import com.chooloo.www.chooloolib.utils.LoadingState

@Composable
fun <ItemType> ChoicePreferenceListItem(
    title: String,
    values: List<ItemType>,
    selectedValue: ItemType?,
    enabled: Boolean = true,
    subtitle: String? = null,
    onValueSelected: (ItemType) -> Unit,
    loadingState: LoadingState = LoadingState.IDLE,
    item: @Composable (item: ItemType, selected: Boolean, onClick: () -> Unit) -> Unit = { _, _, _ -> }
) {
    val choicesVisible = remember { mutableStateOf(false) }

    ListItem(
        clickable = true,
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
                item(it, it == selectedValue) { onValueSelected(it) }
            }
        )
    }
}