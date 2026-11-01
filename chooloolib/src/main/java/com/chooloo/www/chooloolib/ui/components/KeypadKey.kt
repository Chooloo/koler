package com.chooloo.www.chooloolib.ui.components

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun KeypadKey(
    value: String,
    onClick: () -> Unit = {}
) {
    TextButton(
        onClick = onClick,
        shape = CircleShape,
        colors = ButtonDefaults.textButtonColors(colorScheme.surfaceVariant),
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .aspectRatio(1f)
    ) {
        Text(
            text = value,
            style = typography.displayMedium,
        )
    }
}

@Preview
@Composable
fun PreviewDialpadKey() {
    KeypadKey(value = "1")
}
