package com.chooloo.www.chooloolib.ui.contacts

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import coil3.compose.rememberAsyncImagePainter
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.domain.model.ContactData
import com.chooloo.www.chooloolib.ui.components.CircleImage
import com.chooloo.www.chooloolib.ui.components.list.ListItem
import com.chooloo.www.chooloolib.ui.theme.AvatarSize
import com.chooloo.www.chooloolib.ui.theme.Spacing

@Composable
fun ContactListItem(
    modifier: Modifier = Modifier,
    contactData: ContactData,
    highlightText: String? = null,
    onClick: () -> Unit = {},
    onLongClick: () -> Unit = {}
) {
    val defaultPainter = rememberVectorPainter(Icons.Rounded.Person)
    val avatarPainter = if (contactData.imageUri != null) {
        rememberAsyncImagePainter(
            model = contactData.imageUri,
            error = defaultPainter,
            placeholder = defaultPainter
        )
    } else {
        defaultPainter
    }

    ListItem(
        modifier = modifier,
        subtitle = contactData.number,
        highlightText = highlightText,
        title = contactData.name ?: stringResource(id = R.string.contact_unknown_name),
        onClick = onClick,
        onLongClick = onLongClick,
        startContainer = {
            Box(
                modifier = Modifier.padding(end = Spacing.Medium)
            ) {
                CircleImage(
                    modifier = Modifier.size(AvatarSize.Medium),
                    painter = avatarPainter
                )
            }
        }
    )
}

@Preview
@Composable
fun ContactListItemPreview() {
    ContactListItem(
        contactData = ContactData(id = 0, name = "Nisim Kaka", number = "420")
    )
}
