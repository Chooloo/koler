package com.chooloo.www.chooloolib.ui.components

import androidx.activity.compose.BackHandler
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.ui.theme.Spacing

@Composable
fun FloatingBottomBar(
    isSearching: Boolean,
    searchText: String,
    @StringRes searchHintRes: Int,
    onSearchOpen: () -> Unit,
    onSearchTextChange: (String) -> Unit,
    onSearchClose: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit,
) {
    BackHandler(enabled = isSearching, onBack = onSearchClose)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = Spacing.Small, vertical = Spacing.Medium),
    ) {
        Reveal(visible = !isSearching, hiddenScale = 1f) {
            Box(modifier = Modifier.fillMaxWidth()) {
                content()
                FloatingBubble(
                    icon = Icons.Rounded.Search,
                    contentDescription = stringResource(R.string.cd_search),
                    onClick = onSearchOpen,
                    modifier = Modifier.align(Alignment.CenterEnd),
                )
            }
        }
        Reveal(visible = isSearching, hiddenScale = 1f) {
            FloatingSearchField(
                text = searchText,
                hintRes = searchHintRes,
                onTextChange = onSearchTextChange,
                onClose = onSearchClose,
            )
        }
    }
}
