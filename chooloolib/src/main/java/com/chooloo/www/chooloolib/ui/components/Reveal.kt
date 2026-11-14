package com.chooloo.www.chooloolib.ui.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import com.chooloo.www.chooloolib.ui.theme.Layout

@Composable
fun Reveal(
    visible: Boolean,
    modifier: Modifier = Modifier,
    hiddenOffsetX: Dp = 0.dp,
    hiddenScale: Float = Layout.REVEAL_HIDDEN_SCALE,
    content: @Composable () -> Unit,
) {
    val progress by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioNoBouncy,
            stiffness = Spring.StiffnessMediumLow,
        ),
        label = "reveal",
    )
    val offsetPx = with(LocalDensity.current) { hiddenOffsetX.toPx() }
    Box(
        modifier = modifier.graphicsLayer {
            // ModulateAlpha fades without an offscreen buffer, which would crop shadows outside the bounds.
            compositingStrategy = CompositingStrategy.ModulateAlpha
            alpha = progress
            scaleX = lerp(hiddenScale, 1f, progress)
            scaleY = lerp(hiddenScale, 1f, progress)
            translationX = (1f - progress) * offsetPx
        },
    ) {
        if (progress > 0f) content()
    }
}
