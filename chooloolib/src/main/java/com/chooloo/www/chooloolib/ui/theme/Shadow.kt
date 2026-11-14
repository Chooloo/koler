package com.chooloo.www.chooloolib.ui.theme

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp

fun Modifier.softShadow(shape: Shape = CircleShape): Modifier = dropShadow(
    shape,
    Shadow(
        radius = 12.dp,
        color = Color.Black.copy(alpha = 0.12f),
        offset = DpOffset(0.dp, 3.dp),
    ),
)
    .dropShadow(shape, Shadow(radius = 1.dp, color = Color.Black.copy(alpha = 0.10f)))
