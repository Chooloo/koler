package com.chooloo.www.chooloolib.ui.dialpad

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.InterceptPlatformTextInput
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import com.chooloo.www.chooloolib.ui.theme.Spacing
import kotlinx.coroutines.awaitCancellation

@Composable
fun DialpadField(input: DialpadInput, onInputChange: (DialpadInput) -> Unit) {
    InterceptPlatformTextInput(interceptor = { _, _ -> awaitCancellation() }) {
        BasicTextField(
            value = TextFieldValue(input.text, TextRange(input.selectionStart, input.selectionEnd)),
            onValueChange = {
                onInputChange(DialpadInput(it.text, it.selection.start, it.selection.end))
            },
            singleLine = true,
            textStyle = MaterialTheme.typography.headlineLarge.copy(
                color = LocalContentColor.current,
                textAlign = TextAlign.Center,
                textDirection = TextDirection.Ltr,
            ),
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = Spacing.Medium),
        )
    }
}
