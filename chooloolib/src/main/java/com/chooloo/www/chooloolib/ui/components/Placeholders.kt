package com.chooloo.www.chooloolib.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.ui.theme.Spacing

@Composable
fun Loading(modifier: Modifier = Modifier, @StringRes titleRes: Int? = null) {
    CenteredColumn(modifier) {
        CircularProgressIndicator()
        PlaceholderText(titleRes ?: R.string.loading)
    }
}

@Composable
fun Empty(
    modifier: Modifier = Modifier,
    @StringRes titleRes: Int?,
    image: @Composable (() -> Unit)? = null,
) {
    CenteredColumn(modifier) {
        image?.invoke()
        titleRes?.let { PlaceholderText(it) }
    }
}

@Composable
private fun CenteredColumn(modifier: Modifier, content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(Spacing.Medium, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
        content = content,
    )
}

@Composable
private fun PlaceholderText(@StringRes titleRes: Int) {
    Text(
        text = stringResource(titleRes),
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
}
