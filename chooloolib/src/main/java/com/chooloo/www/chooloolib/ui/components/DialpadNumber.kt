package com.chooloo.www.chooloolib.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.chooloo.www.chooloolib.ui.theme.Spacing

@Composable
fun DialpadNumber(
    number: String,
    modifier: Modifier = Modifier,
) {
    val style = MaterialTheme.typography.headlineLarge
    Text(
        text = number,
        maxLines = 1,
        style = style,
        textAlign = TextAlign.Center,
        autoSize = TextAutoSize.StepBased(minFontSize = MaterialTheme.typography.titleMedium.fontSize, maxFontSize = style.fontSize),
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = Spacing.Medium),
    )
}
