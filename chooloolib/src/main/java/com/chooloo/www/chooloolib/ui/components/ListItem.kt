package com.chooloo.www.chooloolib.ui.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.ui.components.CircleImage

@Composable
fun ListItem(
    title: String,
    caption: String,
    imagePainter: Painter? = null,
    captionImagePainter: Painter? = null,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .padding(10.dp)
            .clickable(onClick = onClick::invoke),
        shape = RoundedCornerShape(corner = CornerSize(dimensionResource(id = R.dimen.corner_radius))),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .padding(dimensionResource(id = R.dimen.default_spacing_medium))
        ) {
            CircleImage(painter = imagePainter ?: painterResource(id = R.drawable.person))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimensionResource(id = R.dimen.default_spacing_small_plus))
            ) {
                Text(
                    text = title,
                    style = typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = caption, style = typography.bodySmall)
                    captionImagePainter?.let {
                        Icon(
                            painter = it,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun ListItemPreview() {
    MaterialTheme {
        ListItem(
            title = "List item title",
            caption = "This is a list item",
            imagePainter = painterResource(id = R.drawable.person)
        )
    }
}