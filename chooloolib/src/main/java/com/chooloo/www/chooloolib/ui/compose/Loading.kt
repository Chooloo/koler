package com.chooloo.www.chooloolib.ui.compose

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.chooloo.www.chooloolib.R

@Composable
fun Loading(
    modifier: Modifier = Modifier,
    @StringRes titleRes: Int? = null
) {
    Column(modifier = modifier) {
        CircularProgressIndicator()
        Text(stringResource(id = titleRes ?: R.string.loading))
    }
}