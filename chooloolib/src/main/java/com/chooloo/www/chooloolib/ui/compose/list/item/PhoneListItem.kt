package com.chooloo.www.chooloolib.ui.compose.list.item

import android.provider.ContactsContract.CommonDataKinds.Phone
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Call
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.chooloo.www.chooloolib.domain.model.PhoneData

@Composable
fun PhoneListItem(
    modifier: Modifier = Modifier,
    phoneItem: PhoneData,
    highlightText: String? = null,
    onClick: () -> Unit = {},
    onLongClick: () -> Unit = {},
    onOpenClick: () -> Unit = {}
) {
    val resources = LocalContext.current.resources

    ListItem(
        modifier = modifier,
        title = phoneItem.number,
        highlightText = highlightText,
        subtitle = Phone.getTypeLabel(resources, phoneItem.type, phoneItem.label).toString(),
        onClick = onClick,
        onLongClick = onLongClick,
        endContainer = {
            IconButton(onClick = onOpenClick) {
                Icon(Icons.Rounded.Call, "")
            }
        },
    )
}