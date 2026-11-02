package com.chooloo.www.chooloolib.ui.components

import android.widget.Toast
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext

@Composable
fun MessageToast(@StringRes message: Int?, onShown: () -> Unit) {
    val context = LocalContext.current
    LaunchedEffect(message) {
        message ?: return@LaunchedEffect
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        onShown()
    }
}
