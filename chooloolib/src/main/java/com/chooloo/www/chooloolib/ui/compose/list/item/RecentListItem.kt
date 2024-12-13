package com.chooloo.www.chooloolib.ui.compose.list.item

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.CallMade
import androidx.compose.material.icons.automirrored.rounded.CallMissed
import androidx.compose.material.icons.automirrored.rounded.CallReceived
import androidx.compose.material.icons.rounded.Block
import androidx.compose.material.icons.rounded.CallEnd
import androidx.compose.material.icons.rounded.QuestionMark
import androidx.compose.material.icons.rounded.Voicemail
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.domain.model.RecentData
import com.chooloo.www.chooloolib.domain.model.record.RecentRecord.Companion.TYPE_BLOCKED
import com.chooloo.www.chooloolib.domain.model.record.RecentRecord.Companion.TYPE_INCOMING
import com.chooloo.www.chooloolib.domain.model.record.RecentRecord.Companion.TYPE_MISSED
import com.chooloo.www.chooloolib.domain.model.record.RecentRecord.Companion.TYPE_OUTGOING
import com.chooloo.www.chooloolib.domain.model.record.RecentRecord.Companion.TYPE_REJECTED
import com.chooloo.www.chooloolib.domain.model.record.RecentRecord.Companion.TYPE_VOICEMAIL
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

    val callTypeIcon = when (recentData.type) {
        TYPE_BLOCKED -> Icons.Rounded.Block
        TYPE_REJECTED -> Icons.Rounded.CallEnd
        TYPE_VOICEMAIL -> Icons.Rounded.Voicemail
        TYPE_MISSED -> Icons.AutoMirrored.Rounded.CallMissed
        TYPE_OUTGOING -> Icons.AutoMirrored.Rounded.CallMade
        TYPE_INCOMING -> Icons.AutoMirrored.Rounded.CallReceived
        else -> Icons.Rounded.QuestionMark
    }

    val callTypeIconTint = when (recentData.type) {
        TYPE_MISSED -> colorResource(R.color.negative)
        TYPE_BLOCKED -> colorResource(R.color.negative)
        TYPE_REJECTED -> colorResource(R.color.negative)
        TYPE_OUTGOING -> colorResource(R.color.positive)
        TYPE_INCOMING -> colorResource(R.color.positive)
        TYPE_VOICEMAIL -> colorResource(R.color.positive)
        else -> colorResource(R.color.primary)
    }
    
    ListItem(
        modifier = modifier,
        subtitle = subtitle,
        title = recentData.name ?: "",
        highlightText = highlightText,
        onClick = onClick,
        onLongClick = onLongClick,
        endContainer = {
            Icon(
                tint = callTypeIconTint,
                imageVector = callTypeIcon,
                contentDescription = recentData.typeLabel
            )
        }
    )
}