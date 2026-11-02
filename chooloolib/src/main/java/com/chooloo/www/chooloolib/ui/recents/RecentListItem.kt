package com.chooloo.www.chooloolib.ui.recents

import android.provider.ContactsContract.CommonDataKinds.Phone
import android.text.format.DateUtils
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.domain.model.Recent
import com.chooloo.www.chooloolib.domain.model.RecentGroup
import com.chooloo.www.chooloolib.ui.common.fallbackNameRes
import com.chooloo.www.chooloolib.ui.components.list.ListItem
import com.chooloo.www.chooloolib.ui.theme.ChoolooTheme

@Composable
fun RecentListItem(
    modifier: Modifier = Modifier,
    group: RecentGroup,
    highlightText: String? = null,
    onClick: () -> Unit = {},
    onLongClick: () -> Unit = {}
) {
    val context = LocalContext.current
    val recent = group.latest
    val typeLabel = recent.numberType?.let {
        Phone.getTypeLabel(context.resources, it, recent.numberLabel).toString()
    }
    val subtitle = listOfNotNull(
        DateUtils.formatDateTime(context, recent.dateMillis, DateUtils.FORMAT_SHOW_TIME),
        group.recents.size.takeIf { it > 1 }?.let { "($it)" },
        typeLabel,
    ).joinToString(" · ")

    val callTypeIcon = when (recent.type) {
        Recent.Type.BLOCKED -> Icons.Rounded.Block
        Recent.Type.REJECTED -> Icons.Rounded.CallEnd
        Recent.Type.VOICEMAIL -> Icons.Rounded.Voicemail
        Recent.Type.MISSED -> Icons.AutoMirrored.Rounded.CallMissed
        Recent.Type.OUTGOING -> Icons.AutoMirrored.Rounded.CallMade
        Recent.Type.INCOMING -> Icons.AutoMirrored.Rounded.CallReceived
        Recent.Type.UNKNOWN -> Icons.Rounded.QuestionMark
    }

    val callTypeIconTint = when (recent.type) {
        Recent.Type.MISSED, Recent.Type.BLOCKED, Recent.Type.REJECTED -> ChoolooTheme.extendedColors.negative
        Recent.Type.OUTGOING, Recent.Type.INCOMING, Recent.Type.VOICEMAIL -> ChoolooTheme.extendedColors.positive
        Recent.Type.UNKNOWN -> MaterialTheme.colorScheme.primary
    }

    ListItem(
        modifier = modifier,
        subtitle = subtitle,
        title = recent.name
            ?: recent.presentation.fallbackNameRes?.let { stringResource(it) }
            ?: recent.number.ifEmpty { stringResource(R.string.caller_unknown) },
        highlightText = highlightText,
        onClick = onClick,
        onLongClick = onLongClick,
        endContainer = {
            Icon(tint = callTypeIconTint, imageVector = callTypeIcon, contentDescription = typeLabel)
        }
    )
}
