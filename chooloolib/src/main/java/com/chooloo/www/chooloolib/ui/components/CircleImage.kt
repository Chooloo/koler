package com.chooloo.www.chooloolib.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter

@Composable
fun CircleImage(
    modifier: Modifier = Modifier,
    painter: Painter,
    colorFilter: ColorFilter? = null
) {
    Image(
        painter = painter,
        contentDescription = null,
        colorFilter = colorFilter,
        modifier = modifier
            .aspectRatio(1f)
            .clip(shape = CircleShape)
            .background(MaterialTheme.colorScheme.surfaceVariant)
    )
}