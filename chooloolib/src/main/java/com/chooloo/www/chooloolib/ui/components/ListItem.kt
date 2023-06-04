package com.chooloo.www.chooloolib.ui.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
            .padding(vertical = 12.dp, horizontal = 14.dp)
            .clickable(enabled = true, onClick = onClick::invoke),
        shape = RoundedCornerShape(corner = CornerSize(dimensionResource(id = R.dimen.corner_radius))),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Row(
            verticalAlignment = CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
        ) {
            CircleImage(painter = imagePainter ?: painterResource(id = R.drawable.person))

            Column(
                verticalArrangement = Arrangement.spacedBy(2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp)
            ) {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    style = typography.titleMedium,
                    modifier = Modifier
                        .wrapContentHeight(align = CenterVertically)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(align = CenterVertically)
                ) {
                    Text(
                        text = caption,
                        textAlign = TextAlign.Center,
                        style = typography.labelSmall,
                        modifier = Modifier
                            .padding(top = 1.dp)
                            .wrapContentHeight(align = CenterVertically)
                    )

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