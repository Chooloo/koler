package com.chooloo.www.chooloolib.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.chooloo.www.chooloolib.R

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    text: String,
    @StringRes hintStringRes: Int,
    onTextChange: (String) -> Unit = {},
) {
    val focusManager = LocalFocusManager.current
    var isFocused by remember { mutableStateOf(false) }

    TextField(
        value = text,
        singleLine = true,
        onValueChange = onTextChange,
        shape = MaterialTheme.shapes.large,
        placeholder = { Text(text = stringResource(hintStringRes)) },
        leadingIcon = {
            Icon(
                contentDescription = null,
                imageVector = if (isFocused) Icons.AutoMirrored.Filled.ArrowBack else Icons.Rounded.Search,
                modifier = Modifier.clickable { focusManager.clearFocus() }
            )
        },
        trailingIcon = {
            if (isFocused && text.isNotEmpty()) {
                Icon(
                    contentDescription = null,
                    imageVector = Icons.Rounded.Clear,
                    modifier = Modifier.clickable { onTextChange("") }
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
            .onFocusChanged { isFocused = it.isFocused }
    )
}

@Preview
@Composable
fun SearchBarPreview() {
    SearchBar(text = "", hintStringRes = R.string.hint_search)
}
