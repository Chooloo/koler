package com.chooloo.www.chooloolib.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource

@Composable
fun Empty(@StringRes titleRes: Int?, imageVector: ImageVector?) {
    Column {
        titleRes?.let { Text(text = stringResource(id = it)) }
        imageVector?.let { Icon(imageVector = it, contentDescription = null) }
    }
}