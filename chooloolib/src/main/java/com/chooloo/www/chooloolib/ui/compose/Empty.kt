package com.chooloo.www.chooloolib.ui.compose

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource

@Composable
fun Empty(
    modifier: Modifier = Modifier,
    @StringRes titleRes: Int?,
    image: @Composable (() -> Unit)? = null
) {
    Column(modifier = modifier) {
        titleRes?.let { Text(text = stringResource(id = it)) }
        image?.let { it() }
    }
}