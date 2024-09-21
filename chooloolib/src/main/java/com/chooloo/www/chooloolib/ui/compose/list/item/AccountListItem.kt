package com.chooloo.www.chooloolib.ui.compose.list.item

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.SwitchAccount
import androidx.compose.material.icons.rounded.Whatsapp
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.domain.model.AccountData
import com.chooloo.www.chooloolib.domain.model.record.RawContactRecord

@Composable
fun AccountListItem(
    modifier: Modifier = Modifier,
    accountData: AccountData,
    onClick: () -> Unit = {},
    onLongClick: () -> Unit = {},
) {
    val icon = when (accountData.type) {
        RawContactRecord.RawContactType.WHATSAPP -> Icons.Rounded.Whatsapp
        else -> Icons.Rounded.SwitchAccount
    }

    ListItem(
        onClick = onClick,
        modifier = modifier,
        onLongClick = onLongClick,
        title = stringResource(accountData.type.titleStringRes),
        startContainer = { Icon(icon, stringResource(id = R.string.cd_account_icon)) },
    )
}