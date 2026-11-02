package com.chooloo.www.chooloolib.ui.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.chooloo.www.chooloolib.ui.theme.Spacing

@Composable
fun DialpadTextField(
    modifier: Modifier = Modifier,
    text: String,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = Spacing.Medium)
    ) {
        Text(
            text = text,
            maxLines = 1,
            style = typography.headlineLarge,
            modifier = Modifier.horizontalScroll(rememberScrollState())
        )
    }
}

@Preview
@Composable
fun DialpadTextFieldPreview() {
    DialpadTextField(text = "0541234567")
}
