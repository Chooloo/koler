package com.chooloo.www.chooloolib.ui.compose

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.chooloo.www.chooloolib.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    text: String? = null,
    @StringRes hintStringRes: Int? = null,
    onTextChange: (String) -> Unit = {},
    onFocusChange: (Boolean) -> Unit = {},
) {
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    val isFocused = remember { mutableStateOf(false) }
    val shouldShowClearIcon = isFocused.value && text?.isNotEmpty() == true
    val leadingIconVector =
        if (isFocused.value) Icons.AutoMirrored.Filled.ArrowBack else Icons.Rounded.Search

    fun onClearClick() {
        onTextChange("")
    }

    fun onLeadingIconClick() {
        if (isFocused.value) {
            focusManager.clearFocus()
        }
    }

    fun validateChar(char: Char) =
        if (!(char.isLetterOrDigit() || arrayOf('+', ' ').contains(char))) "" else char

    fun onValueChange(value: String) {
        onTextChange(value.map(::validateChar).joinToString(""))
    }

    fun onFocusChanged(focused: Boolean) {
        onFocusChange(focused)
        isFocused.value = focused
    }


    TextField(
        value = text ?: "",
        onValueChange = ::onValueChange,
        shape = RoundedCornerShape(dimensionResource(R.dimen.corner_radius)),
        placeholder = { hintStringRes?.let { stringResource(it) } },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        leadingIcon = {
            Icon(
                contentDescription = "",
                imageVector = leadingIconVector,
                modifier = Modifier.clickable(onClick = ::onLeadingIconClick)
            )
        },
        trailingIcon = {
            if (shouldShowClearIcon) {
                Icon(
                    contentDescription = "",
                    imageVector = Icons.Rounded.Clear,
                    modifier = Modifier.clickable(onClick = ::onClearClick)
                )
            }
        },
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        modifier = modifier
            .fillMaxWidth()
            .focusable(true)
            .focusRequester(focusRequester)
            .onFocusChanged { onFocusChanged(it.isFocused) }
    )
}

@Preview
@Composable
fun SearchBarPreview() {
    SearchBar(hintStringRes = R.string.hint_search)
}