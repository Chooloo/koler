package com.chooloo.www.chooloolib.ui.compose

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.chooloo.www.chooloolib.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialpadTextField(
    modifier: Modifier = Modifier,
    text: String? = null,
    @StringRes hintStringRes: Int? = null,
    onTextChange: (String) -> Unit = {},
    onFocusChange: (Boolean) -> Unit = {},
) {
    val focusRequester = remember { FocusRequester() }
    val isFocused = remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val hintText = hintStringRes?.let { stringResource(id = hintStringRes) }

    fun validateChar(char: Char) =
        if (!(char.isDigit() || arrayOf('+', '#', '*').contains(char))) {
            ""
        } else {
            char
        }

    TextField(
        maxLines = 1,
        value = text ?: "",
        placeholder = { hintText?.let { Text(it) } },
        shape = RoundedCornerShape(dimensionResource(R.dimen.corner_radius)),
        textStyle = typography.headlineLarge.copy(textAlign = TextAlign.Center),
        onValueChange = { value ->
            onTextChange(value.map(::validateChar).joinToString(""))
        },
        colors = TextFieldDefaults.colors(
            disabledTextColor = Color.Transparent,
            focusedContainerColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
        ),
        modifier = modifier
            .fillMaxWidth()
            .focusable(true)
            .background(Color.Transparent)
            .focusRequester(focusRequester)
            .horizontalScroll(rememberScrollState())
            .onFocusChanged {
                onFocusChange(it.isFocused)
                isFocused.value = it.isFocused
            },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() })
    )
}

@Preview
@Composable
fun DialpadTextFieldPreview() {
    DialpadTextField(text = "ssss")
}