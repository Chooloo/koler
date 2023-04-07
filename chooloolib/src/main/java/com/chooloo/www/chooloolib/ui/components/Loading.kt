package com.chooloo.www.chooloolib.ui.components

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.chooloo.www.chooloolib.R

@Composable
fun Loading() {
    CircularProgressIndicator()
    Text(stringResource(id = R.string.loading))
}