package com.chooloo.www.chooloolib.ui.compose

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.lerp
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.launch
import java.util.Locale

@Composable
fun Tab(
    text: String,
    isSelected: Boolean = false,
    onClickListener: () -> Unit = {}
) {
    val scope = rememberCoroutineScope()

    val textStyleIdle = typography.titleSmall
    val textStyleSelected = typography.titleLarge.copy(fontWeight = FontWeight.Bold)
    val textStyleAnimation = remember { Animatable(0f) }
    val textStyle by remember(textStyleAnimation.value) {
        derivedStateOf { lerp(textStyleIdle, textStyleSelected, textStyleAnimation.value) }
    }

    val textColor: Color by animateColorAsState(
        label = "",
        animationSpec = tween(1000, easing = LinearEasing),
        targetValue = if (isSelected) colorScheme.onSurface else colorScheme.onSurfaceVariant,
    )

    LaunchedEffect(key1 = isSelected) {
        scope.launch {
            textStyleAnimation.animateTo(if (isSelected) 1f else 0f, tween(500))
        }
    }

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