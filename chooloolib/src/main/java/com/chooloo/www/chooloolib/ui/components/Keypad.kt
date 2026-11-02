package com.chooloo.www.chooloolib.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.chooloo.www.chooloolib.ui.theme.Spacing

private val KEYS = "123456789*0#".toList()

@Composable
fun Keypad(
    modifier: Modifier = Modifier,
    onKeyClick: (key: Char) -> Unit = {},
    onKeyLongClick: ((key: Char) -> Unit)? = null,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = modifier.wrapContentWidth(),
        verticalArrangement = Arrangement.spacedBy(Spacing.Medium),
        horizontalArrangement = Arrangement.spacedBy(Spacing.Medium),
        contentPadding = PaddingValues(
            vertical = Spacing.Medium,
            horizontal = Spacing.ExtraLarge
        ),
        content = {
            items(KEYS) { key ->
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.wrapContentWidth()
                ) {
                    KeypadKey(
                        key = key,
                        onClick = { onKeyClick(key) },
                        onLongClick = onKeyLongClick?.let { { it(key) } },
                    )
                }
            }
        },
    )
}

@Preview
@Composable
fun DialpadPreview() {
    Keypad()
}
