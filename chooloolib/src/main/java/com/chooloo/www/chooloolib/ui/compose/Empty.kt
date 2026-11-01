package com.chooloo.www.chooloolib.ui.compose

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.chooloo.www.chooloolib.ui.theme.Spacing

@Composable
fun Empty(
    modifier: Modifier = Modifier,
    @StringRes titleRes: Int?,
    image: @Composable (() -> Unit)? = null
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(
            Spacing.Medium,
            Alignment.CenterVertically
        ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        image?.let { it() }
        titleRes?.let {
            Text(
                text = stringResource(id = it),
                style = typography.bodyLarge,
                color = colorScheme.onSurfaceVariant
            )
        }
    }
}
