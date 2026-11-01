package com.chooloo.www.chooloolib.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.chooloo.www.chooloolib.ui.theme.Spacing

@Composable
fun Tabs(
    modifier: Modifier = Modifier,
    headers: List<String>,
    selectedTabIndex: Int? = null,
    onTabClick: (index: Int, header: String) -> Unit = { _, _ -> }
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.spacedBy(Spacing.Small)
    ) {
        headers.forEachIndexed { index, tab ->
            Tab(
                text = tab,
                isSelected = index == selectedTabIndex,
                onClickListener = { onTabClick(index, tab) }
            )
        }
    }
}

@Preview
@Composable
fun TabsPreview() {
    Tabs(headers = listOf("Header1", "Header2"), selectedTabIndex = 1)
}