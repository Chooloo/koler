package com.chooloo.www.chooloolib.ui.caller

import android.provider.ContactsContract.CommonDataKinds.Phone as PhoneContract
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Call
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalResources
import com.chooloo.www.chooloolib.domain.model.Phone
import com.chooloo.www.chooloolib.ui.components.list.ListItem

@Composable
fun PhoneListItem(
    modifier: Modifier = Modifier,
    phone: Phone,
    onClick: () -> Unit = {},
    onLongClick: () -> Unit = {},
    onOpenClick: () -> Unit = {}
) {
    val resources = LocalResources.current

    ListItem(
        modifier = modifier,
        title = phone.number,
        subtitle = PhoneContract.getTypeLabel(resources, phone.type, phone.label).toString(),
        onClick = onClick,
        onLongClick = onLongClick,
        endContainer = {
            IconButton(onClick = onOpenClick) {
                Icon(Icons.Rounded.Call, "")
            }
        },
    )
}
