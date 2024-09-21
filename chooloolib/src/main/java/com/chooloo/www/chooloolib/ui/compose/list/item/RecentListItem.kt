package com.chooloo.www.chooloolib.ui.compose.list.item

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.chooloo.www.chooloolib.domain.model.RecentData
import com.chooloo.www.chooloolib.utils.getHoursString

@Composable
fun RecentListItem(
    modifier: Modifier = Modifier,
    recentData: RecentData,
    highlightText: String? = null,
    onClick: () -> Unit = {},
    onLongClick: () -> Unit = {}
) {
    val context = LocalContext.current

    val subtitle = run {
        val parts = mutableListOf("${context.getHoursString(recentData.date)} ·")
        if (recentData.groupAccounts.count() > 1) {
            parts.add("(${recentData.groupAccounts.count()})")
        }
        recentData.typeLabel?.let {
            parts.add("${recentData.typeLabel}")
        }
        parts.joinToString(" · ")
    }

    ListItem(
        modifier = modifier,
        subtitle = subtitle,
        title = recentData.name ?: "",
        highlightText = highlightText,
        onClick = onClick,
        onLongClick = onLongClick,
    )
}