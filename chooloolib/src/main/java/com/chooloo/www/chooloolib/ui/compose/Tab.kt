package com.chooloo.www.chooloolib.ui.compose

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.lerp
import androidx.compose.ui.tooling.preview.Preview
import java.util.Locale

@Composable
fun Tab(
    text: String,
    isSelected: Boolean = false,
    onClickListener: () -> Unit = {}
) {
    val textStyleIdle = typography.titleSmall
    val textStyleSelected = typography.titleLarge.copy(fontWeight = FontWeight.Bold)
    val fraction by animateFloatAsState(
        label = "tabStyleFraction",
        targetValue = if (isSelected) 1f else 0f,
        animationSpec = tween(300)
    )
    val textStyle by remember(fraction, textStyleIdle, textStyleSelected) {
        derivedStateOf { lerp(textStyleIdle, textStyleSelected, fraction) }
    }

    val textColor: Color by animateColorAsState(
        label = "tabColor",
        animationSpec = tween(300),
        targetValue = if (isSelected) colorScheme.onSurface else colorScheme.onSurfaceVariant,
    )

    Text(
        color = textColor,
        style = textStyle,
        modifier = Modifier.clickable(onClick = onClickListener),
        text = text.replaceFirstChar { it.uppercase(Locale.ROOT) },
    )
}

@Preview
@Composable
fun TabPreview() {
    Tab(text = "TabHeader")
}

@Preview
@Composable
fun TabPreviewSelected() {
    Tab(text = "TabHeader", isSelected = true)
}
