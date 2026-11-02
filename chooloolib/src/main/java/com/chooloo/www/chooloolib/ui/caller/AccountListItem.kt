package com.chooloo.www.chooloolib.ui.caller

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.SwitchAccount
import androidx.compose.material.icons.rounded.Whatsapp
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.domain.model.ContactAccount
import com.chooloo.www.chooloolib.ui.common.titleRes
import com.chooloo.www.chooloolib.ui.components.list.ListItem

@Composable
fun AccountListItem(
    modifier: Modifier = Modifier,
    account: ContactAccount,
    onClick: () -> Unit = {},
    onLongClick: () -> Unit = {},
) {
    val icon = when (account.type) {
        ContactAccount.Type.WHATSAPP -> Icons.Rounded.Whatsapp
        else -> Icons.Rounded.SwitchAccount
    }

    ListItem(
        onClick = onClick,
        modifier = modifier,
        onLongClick = onLongClick,
        title = stringResource(account.type.titleRes),
        startContainer = { Icon(icon, stringResource(id = R.string.cd_account_icon)) },
    )
}
