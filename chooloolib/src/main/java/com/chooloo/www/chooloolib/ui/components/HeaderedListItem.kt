package com.chooloo.www.chooloolib.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.ui.list.ListItem

@Composable
fun HeaderedListItem(
    title: String,
    caption: String,
    header: String? = null,
    imagePainter: Painter? = null,
    captionImagePainter: Painter? = null,
    onClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier.padding(horizontal = 6.dp)
    ) {
        header?.let {
            Text(
                text = it,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 18.dp)
            )
        }

        ListItem(
            title = title,
            caption = caption,
            imagePainter = imagePainter,
            captionImagePainter = captionImagePainter,
            onClick = onClick
        )
    }
}

@Preview
@Composable
private fun HeaderedListItemPreview() {
    MaterialTheme {
        HeaderedListItem(
            header = "s",
            title = "List item title",
            caption = "This is a list item",
            imagePainter = painterResource(id = R.drawable.person)
        )
    }
}