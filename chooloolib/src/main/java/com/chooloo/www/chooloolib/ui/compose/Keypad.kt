package com.chooloo.www.chooloolib.ui.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import com.chooloo.www.chooloolib.R

val DIALPAD_KEYS = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "*", "0", "#")

@Composable
fun Keypad(
    modifier: Modifier = Modifier,
    onKeyClick: (key: Char) -> Unit = {}
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = modifier.wrapContentWidth(),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.spacing_small)),
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.spacing_small)),
        contentPadding = PaddingValues(
            vertical = dimensionResource(id = R.dimen.spacing),
            horizontal = dimensionResource(id = R.dimen.spacing_big)
        ),
        content = {
            items(12) { index ->
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.wrapContentWidth()
                ) {
                    val key = DIALPAD_KEYS.getOrElse(index) { "?" }
                    KeypadKey(value = key, onClick = { onKeyClick(key[0]) })
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