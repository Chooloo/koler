package com.chooloo.www.chooloolib.ui.components

import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun KeypadKey(
    key: Char,
    onClick: () -> Unit = {},
    onLongClick: (() -> Unit)? = null,
) {
    Surface(
        shape = CircleShape,
        color = colorScheme.surfaceVariant,
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .aspectRatio(1f)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.combinedClickable(onClick = onClick, onLongClick = onLongClick)
        ) {
            Text(text = key.toString(), style = typography.displayMedium)
        }
    }
}

@Preview
@Composable
fun KeypadKeyPreview() {
    KeypadKey(key = '1')
}
