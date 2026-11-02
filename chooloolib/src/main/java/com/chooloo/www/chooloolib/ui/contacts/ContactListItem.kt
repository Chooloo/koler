package com.chooloo.www.chooloolib.ui.contacts

import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.domain.model.Contact
import com.chooloo.www.chooloolib.ui.components.Avatar
import com.chooloo.www.chooloolib.ui.components.list.ListItem
import com.chooloo.www.chooloolib.ui.theme.Sizes

@Composable
fun ContactListItem(
    contact: Contact,
    onClick: () -> Unit,
    onLongClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
) {
    ListItem(
        modifier = modifier.height(Sizes.CompactListItem),
        subtitle = contact.number,
        title = contact.name ?: stringResource(id = R.string.contact_unknown_name),
        onClick = onClick,
        onLongClick = onLongClick,
        startContainer = {
            Avatar(name = null, photoUri = contact.photoUri, size = Sizes.AvatarSmall)
        }
    )
}
