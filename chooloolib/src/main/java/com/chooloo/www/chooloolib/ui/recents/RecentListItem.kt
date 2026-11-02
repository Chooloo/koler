package com.chooloo.www.chooloolib.ui.recents

import android.provider.ContactsContract.CommonDataKinds.Phone
import android.text.format.DateUtils
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.stringResource
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.domain.model.RecentGroup
import com.chooloo.www.chooloolib.ui.common.fallbackNameRes
import com.chooloo.www.chooloolib.ui.components.list.ListItem
import com.chooloo.www.chooloolib.ui.theme.Sizes

@Composable
fun RecentListItem(
    modifier: Modifier = Modifier,
    group: RecentGroup,
    onClick: () -> Unit = {},
    onLongClick: (() -> Unit)? = null
) {
    val context = LocalContext.current
    val recent = group.latest
    val typeLabel = recent.numberType?.let {
        Phone.getTypeLabel(LocalResources.current, it, recent.numberLabel).toString()
    }
    val subtitle = listOfNotNull(
        DateUtils.formatDateTime(context, recent.dateMillis, DateUtils.FORMAT_SHOW_TIME),
        group.recents.size.takeIf { it > 1 }?.let { "($it)" },
        typeLabel,
    ).joinToString(" · ")

    ListItem(
        modifier = modifier.height(Sizes.CompactListItem),
        subtitle = subtitle,
        title = recent.name
            ?: recent.presentation.fallbackNameRes?.let { stringResource(it) }
            ?: recent.number.ifEmpty { stringResource(R.string.caller_unknown) },
        onClick = onClick,
        onLongClick = onLongClick,
        endContainer = {
            RecentTypeIcon(type = recent.type, contentDescription = typeLabel)
        }
    )
}
