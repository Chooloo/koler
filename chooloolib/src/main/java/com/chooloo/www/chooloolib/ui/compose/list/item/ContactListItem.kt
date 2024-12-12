package com.chooloo.www.chooloolib.ui.compose.list.item

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.domain.model.ContactData
import com.chooloo.www.chooloolib.ui.compose.CircleImage

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
        startContainer = {
            Box(
                modifier = Modifier.padding(
                    start = 0.dp,
                    top = 0.dp,
                    end = dimensionResource(R.dimen.spacing_small),
                    bottom = 0.dp
                )
            ) {
                CircleImage(
                    modifier = Modifier
                        .width(dimensionResource(id = R.dimen.image_size_medium))
                        .height(dimensionResource(id = R.dimen.image_size_medium)),
                    painter = rememberVectorPainter(Icons.Rounded.Person)
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