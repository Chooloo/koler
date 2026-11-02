package com.chooloo.www.chooloolib.ui.components

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.ui.theme.Spacing

@Composable
fun AppBarHeader(
    headers: List<String>,
    selectedTabIndex: Int = 0,
    isSearchVisible: Boolean = true,
    searchText: String = "",
    @StringRes searchHintRes: Int = R.string.hint_search,
    onTabClick: (index: Int) -> Unit = {},
    onMenuClick: () -> Unit = {},
    onSearchTextChange: (String) -> Unit = {}
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = Spacing.Medium,
                    vertical = Spacing.Small
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Tabs(
                headers = headers,
                selectedTabIndex = selectedTabIndex,
                onTabClick = onTabClick
            )

            IconButton(onClick = onMenuClick) {
                Icon(
                    imageVector = Icons.Rounded.Menu,
                    contentDescription = stringResource(id = R.string.cd_show_menu)
                )
            }
        }

        AnimatedVisibility(
            visible = isSearchVisible,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = Spacing.Medium,
                    vertical = Spacing.Small
                )
        ) {
            SearchBar(
                text = searchText,
                hintStringRes = searchHintRes,
                onTextChange = onSearchTextChange,
            )
        }
    }
}
