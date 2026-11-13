package com.chooloo.www.chooloolib.ui.components

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import com.chooloo.www.chooloolib.ui.common.UserMessage

@Composable
fun MessageToast(message: UserMessage?, onShown: () -> Unit) {
    val context = LocalContext.current
    LaunchedEffect(message) {
        message ?: return@LaunchedEffect
        Toast.makeText(context, message.textRes, Toast.LENGTH_SHORT).show()
        onShown()
    }
}
