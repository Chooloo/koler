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
import com.chooloo.www.chooloolib.domain.model.Recent

@Composable
fun RecentTypeIcon(type: Recent.Type, contentDescription: String?, modifier: Modifier = Modifier) {
    val icon = when (type) {
        Recent.Type.BLOCKED -> Icons.Rounded.Block
        Recent.Type.REJECTED -> Icons.Rounded.CallEnd
        Recent.Type.VOICEMAIL -> Icons.Rounded.Voicemail
        Recent.Type.MISSED -> Icons.AutoMirrored.Rounded.CallMissed
        Recent.Type.OUTGOING -> Icons.AutoMirrored.Rounded.CallMade
        Recent.Type.INCOMING -> Icons.AutoMirrored.Rounded.CallReceived
        Recent.Type.UNKNOWN -> Icons.Rounded.QuestionMark
    }
    val tint = when (type) {
        Recent.Type.MISSED, Recent.Type.BLOCKED, Recent.Type.REJECTED -> MaterialTheme.colorScheme.error
        else -> MaterialTheme.colorScheme.onSurfaceVariant
    }
    Icon(imageVector = icon, contentDescription = contentDescription, tint = tint, modifier = modifier)
}
