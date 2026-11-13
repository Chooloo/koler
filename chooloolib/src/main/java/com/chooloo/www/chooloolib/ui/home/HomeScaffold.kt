package com.chooloo.www.chooloolib.ui.home

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.ui.components.FloatingBottomBar
import com.chooloo.www.chooloolib.ui.components.ScreenTopBar
import com.chooloo.www.chooloolib.ui.theme.Sizes

@Composable
fun HomeScaffold(
    title: String,
    isSearching: Boolean,
    searchText: String,
    @StringRes searchHintRes: Int,
    onSearchOpen: () -> Unit,
    onSearchTextChange: (String) -> Unit,
    onSearchClose: () -> Unit,
    onSettingsOpen: () -> Unit,
    bottomBarContent: @Composable BoxScope.() -> Unit,
    content: @Composable ColumnScope.(listPadding: PaddingValues) -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            ScreenTopBar(
                title = title,
                actions = {
                    IconButton(onClick = onSettingsOpen) {
                        Icon(Icons.Rounded.Settings, stringResource(R.string.settings))
                    }
                },
            )
            content(PaddingValues(bottom = Sizes.FloatingBarInset))
        }

        FloatingBottomBar(
            isSearching = isSearching,
            searchText = searchText,
            searchHintRes = searchHintRes,
            onSearchOpen = onSearchOpen,
            onSearchTextChange = onSearchTextChange,
            onSearchClose = onSearchClose,
            modifier = Modifier.align(Alignment.BottomCenter),
            content = bottomBarContent,
        )
    }
}
