package com.chooloo.www.chooloolib.ui.components

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.data.model.ContactAccount
import com.chooloo.www.chooloolib.ui.list.ListItem

@Composable
fun <Item> List(
    items: List<Item>,
    isLoading: Boolean = false,
    emptyImageVector: ImageVector? = null,
    @StringRes emptyStringRes: Int? = null,
    itemBuilder: @Composable (Item) -> Unit,
) {
    if (isLoading) {
        Loading()
    } else if (items.isNotEmpty()) {
        ListColumn(items = items, itemBuilder = itemBuilder)
    } else {
        Empty(titleRes = emptyStringRes, imageVector = emptyImageVector)
    }
}

@Preview
@Composable
fun ListPreview() {
    List(
        items = listOf(ContactAccount(0, "Nahum Test", starred = false), ContactAccount()),
        isLoading = false,
        emptyImageVector = Icons.Default.Person,
        emptyStringRes = R.string.error_no_results_contacts,
        itemBuilder = { contact ->
            ListItem(
                title = contact.name ?: "Unknown",
                caption = contact.starred.toString()
            )
        }
    )
}