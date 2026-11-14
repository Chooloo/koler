package com.chooloo.www.chooloolib.ui.components

import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.min
import com.chooloo.www.chooloolib.ui.theme.Layout
import com.chooloo.www.chooloolib.ui.theme.Sizes
import com.chooloo.www.chooloolib.ui.theme.Spacing

@Composable
fun Keypad(
    modifier: Modifier = Modifier,
    onKeyClick: (key: Char) -> Unit,
    onKeyLongClick: ((key: Char) -> Unit)? = null,
) {
    BoxWithConstraints(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = Spacing.Medium),
    ) {
        val keySize = min((maxWidth - Spacing.Large * 2) / 3, Sizes.KeypadKey)
        Column(verticalArrangement = Arrangement.spacedBy(Spacing.Medium)) {
            Layout.KEYPAD_ROWS.forEach { row ->
                Row(horizontalArrangement = Arrangement.spacedBy(Spacing.Large)) {
                    row.forEach { key ->
                        KeypadKey(
                            key = key,
                            size = keySize,
                            onClick = { onKeyClick(key) },
                            onLongClick = onKeyLongClick?.let { { it(key) } },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun KeypadKey(key: Char, size: Dp, onClick: () -> Unit, onLongClick: (() -> Unit)?) {
    Surface(
        shape = CircleShape,
        color = MaterialTheme.colorScheme.surfaceContainerHigh,
        modifier = Modifier.size(size),
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.combinedClickable(
                role = Role.Button,
                onClick = onClick,
                onLongClick = onLongClick,
            ),
        ) {
            Text(text = key.toString(), style = MaterialTheme.typography.headlineLarge)
        }
    }
}
