package com.chooloo.www.chooloolib.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.chooloo.www.chooloolib.R

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    searchText: String? = null,
    @StringRes hintStringRes: Int,
    onValueChange: (String) -> Unit = {}
) {
    val focusRequester = remember { FocusRequester() }
    var showClearButton = remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val iconRes = if (showClearButton.value) Icons.Default.Search else Icons.Default.ArrowBack

    fun onButtonClick() {
        if (showClearButton.value) {
            focusRequester.freeFocus()
        }
    }

    Row {
        IconButton(onClick = ::onButtonClick) {
            Icon(iconRes, "")
        }

        TextField(
            value = searchText ?: "",
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester)
                .onFocusChanged { showClearButton.value = it.isFocused }
        )
    }
}

@Preview
@Composable
fun SearchBarPreview() {
    SearchBar(
        hintStringRes = R.string.hint_search,
        onValueChange = {}
    )
}