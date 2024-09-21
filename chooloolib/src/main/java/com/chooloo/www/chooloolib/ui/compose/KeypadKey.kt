package com.chooloo.www.chooloolib.ui.compose

import android.view.KeyEvent
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

val DIALPAD_KEY_STR = mapOf(
    KeyEvent.KEYCODE_1 to "1",
    KeyEvent.KEYCODE_2 to "2",
    KeyEvent.KEYCODE_3 to "3",
    KeyEvent.KEYCODE_4 to "4",
    KeyEvent.KEYCODE_5 to "5",
    KeyEvent.KEYCODE_6 to "6",
    KeyEvent.KEYCODE_7 to "7",
    KeyEvent.KEYCODE_8 to "8",
    KeyEvent.KEYCODE_9 to "9",
    KeyEvent.KEYCODE_STAR to "*",
    KeyEvent.KEYCODE_0 to "0",
    KeyEvent.KEYCODE_POUND to "#"
)

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
