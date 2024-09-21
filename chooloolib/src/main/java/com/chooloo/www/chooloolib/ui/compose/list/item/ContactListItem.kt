package com.chooloo.www.chooloolib.ui.compose.list.item

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.domain.model.ContactData

@Composable
fun ContactListItem(
    modifier: Modifier = Modifier,
    contactData: ContactData,
    highlightText: String? = null,
    onClick: () -> Unit = {},
    onLongClick: () -> Unit = {}
) {
    ListItem(
        modifier = modifier,
        subtitle = contactData.number,
        highlightText = highlightText,
        title = contactData.name ?: stringResource(id = R.string.contact_unknown_name),
        onClick = onClick,
        onLongClick = onLongClick,
    )
}