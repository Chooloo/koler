package com.chooloo.www.chooloolib.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.dimensionResource
import com.chooloo.www.chooloolib.R

@Composable
fun CircleImage(painter: Painter) {
    Image(
        painter = painter,
        contentDescription = null,
        modifier = Modifier
            .aspectRatio(1f)
            .clip(shape = CircleShape)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .width(dimensionResource(id = R.dimen.image_size_medium))
            .height(dimensionResource(id = R.dimen.image_size_medium))
    )
}