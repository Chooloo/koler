package com.chooloo.www.chooloolib.ui.recents

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.CallMade
import androidx.compose.material.icons.automirrored.rounded.CallMissed
import androidx.compose.material.icons.automirrored.rounded.CallReceived
import androidx.compose.material.icons.rounded.Block
import androidx.compose.material.icons.rounded.CallEnd
import androidx.compose.material.icons.rounded.QuestionMark
import androidx.compose.material.icons.rounded.Voicemail
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.chooloo.www.chooloolib.domain.enum.RecentType

@Composable
fun RecentTypeIcon(type: RecentType, contentDescription: String?, modifier: Modifier = Modifier) {
    val icon = when (type) {
        RecentType.BLOCKED -> Icons.Rounded.Block
        RecentType.REJECTED -> Icons.Rounded.CallEnd
        RecentType.VOICEMAIL -> Icons.Rounded.Voicemail
        RecentType.MISSED -> Icons.AutoMirrored.Rounded.CallMissed
        RecentType.OUTGOING -> Icons.AutoMirrored.Rounded.CallMade
        RecentType.INCOMING -> Icons.AutoMirrored.Rounded.CallReceived
        RecentType.UNKNOWN -> Icons.Rounded.QuestionMark
    }
    val tint = when (type) {
        RecentType.MISSED, RecentType.BLOCKED, RecentType.REJECTED ->
            MaterialTheme.colorScheme.error

        else -> MaterialTheme.colorScheme.onSurfaceVariant
    }
    Icon(
        imageVector = icon,
        contentDescription = contentDescription,
        tint = tint,
        modifier = modifier,
    )
}
