package com.chooloo.www.chooloolib.ui.compose.list.item

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.utils.getAttrColor


@Composable
fun HeaderListItem(
    modifier: Modifier = Modifier,
    header: String
) {
    Box(
        modifier = modifier
            .padding(
                end = dimensionResource(R.dimen.spacing),
                top = dimensionResource(R.dimen.spacing_small),
                bottom = dimensionResource(R.dimen.spacing_tiny),
                start = dimensionResource(R.dimen.spacing_tiny),
            )
            .clip(RoundedCornerShape(dimensionResource(R.dimen.corner_radius)))
            .background(Color(LocalContext.current.getAttrColor(R.attr.colorSurfaceContainerHigh)))
    ) {
        Text(
            text = header,
            fontWeight = FontWeight.Bold,
            style = typography.labelSmall,
            modifier = Modifier
                .padding(horizontal = dimensionResource(R.dimen.spacing_tiny), vertical = 4.dp)
        )
    }
}

@Preview
@Composable
fun HeaderListItemPreview() {
    HeaderListItem(header = "2 Days Ago")
}