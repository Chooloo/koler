package com.chooloo.www.chooloolib.ui.caller

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Block
import androidx.compose.material.icons.rounded.Call
import androidx.compose.material.icons.rounded.DeleteOutline
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.History
import androidx.compose.material.icons.rounded.PersonAdd
import androidx.compose.material.icons.rounded.Sms
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.StarBorder
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.domain.model.ContactAccount
import com.chooloo.www.chooloolib.domain.model.Phone
import com.chooloo.www.chooloolib.ui.components.CircleImage
import com.chooloo.www.chooloolib.ui.components.Empty
import com.chooloo.www.chooloolib.ui.components.Loading
import com.chooloo.www.chooloolib.ui.components.list.ListItem
import com.chooloo.www.chooloolib.ui.theme.AvatarSize
import com.chooloo.www.chooloolib.ui.theme.Spacing
import com.chooloo.www.chooloolib.utils.LoadingState

@Composable
fun Caller(
    name: String? = null,
    number: String? = null,
    isBlocked: Boolean? = null,
    phones: List<Phone> = listOf(),
    imagePainter: Painter? = null,
    accounts: List<ContactAccount> = listOf(),
    loadingState: LoadingState = LoadingState.IDLE,
    phonesLoadingState: LoadingState = LoadingState.IDLE,
    accountsLoadingState: LoadingState = LoadingState.IDLE,
    onSmsClick: (() -> Unit)? = null,
    onCallClick: (() -> Unit)? = null,
    onHistoryClick: (() -> Unit)? = null,
    onPhoneClick: (Phone) -> Unit = {},
    onPhoneLongClick: (Phone) -> Unit = {},
    onAccountClick: (ContactAccount) -> Unit = {},
    onSetBlockedClick: ((Boolean) -> Unit)? = null,
    isContactFavorite: Boolean = false,
    onEditContactClick: (() -> Unit)? = null,
    onCreateContactClick: (() -> Unit)? = null,
    onDeleteContactClick: (() -> Unit)? = null,
    onSetContactFavoriteClick: ((Boolean) -> Unit)? = null,
) {
    if (loadingState != LoadingState.SUCCESS) {
        when (loadingState) {
            LoadingState.LOADING -> Loading()
            LoadingState.FAILED -> Empty(titleRes = R.string.error_caller_not_found)
            else -> Empty(titleRes = R.string.error_caller_not_provided)
        }
        return
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Spacing.Medium),
            modifier = Modifier.padding(horizontal = Spacing.Medium)
        ) {
            imagePainter?.let {
                CircleImage(
                    painter = it,
                    modifier = Modifier.size(AvatarSize.Medium)
                )
            }
            Column {
                Text(
                    text = name ?: number ?: stringResource(R.string.caller_unknown),
                    style = typography.titleLarge
                )
                if (name != null && number != null) {
                    Text(text = number, style = typography.labelMedium)
                }
            }
        }

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    vertical = Spacing.Medium,
                    horizontal = Spacing.ExtraLarge
                )
        ) {
            ExtendedFloatingActionButton(
                modifier = Modifier.clickable(onCallClick != null) { onCallClick?.invoke() },
                onClick = onCallClick ?: {},
                icon = { Icon(Icons.Rounded.Call, stringResource(id = R.string.cd_call)) },
                text = { Text(stringResource(R.string.action_call)) }
            )

            ExtendedFloatingActionButton(
                modifier = Modifier.clickable(onSmsClick != null) { onSmsClick?.invoke() },
                onClick = onSmsClick ?: {},
                icon = { Icon(Icons.Rounded.Sms, stringResource(id = R.string.cd_sms)) },
                text = { Text(stringResource(R.string.cd_sms)) }
            )

            ExtendedFloatingActionButton(
                modifier = Modifier.clickable(onEditContactClick != null) { onEditContactClick?.invoke() },
                onClick = onEditContactClick ?: {},
                icon = { Icon(Icons.Rounded.Edit, stringResource(R.string.cd_edit)) },
                text = { Text(stringResource(R.string.cd_edit)) }
            )
        }

        if (phones.isNotEmpty()) {
            Text(
                modifier = Modifier.padding(
                    top = Spacing.Medium,
                    start = Spacing.Medium,
                    bottom = Spacing.Small
                ),
                text = stringResource(id = R.string.phones),
                style = typography.labelSmall
            )
            PhonesList(
                modifier = Modifier.padding(horizontal = Spacing.Medium),
                items = phones,
                onItemActionClick = onPhoneClick,
                onItemLongClick = onPhoneLongClick,
                loadingState = phonesLoadingState
            )
        }

        if (accounts.isNotEmpty()) {
            Text(
                modifier = Modifier.padding(
                    top = Spacing.Medium,
                    start = Spacing.Medium,
                    bottom = Spacing.Small
                ),
                style = typography.labelSmall,
                text = stringResource(id = R.string.accounts),
            )
            AccountsList(
                modifier = Modifier.padding(horizontal = Spacing.Medium),
                items = accounts,
                onItemClick = onAccountClick,
                loadingState = accountsLoadingState,
            )
        }

        Column {
            onCreateContactClick?.let {
                ListItem(
                    onClick = it,
                    title = stringResource(R.string.add_contact),
                    startContainer = {
                        Icon(
                            imageVector = Icons.Rounded.PersonAdd,
                            contentDescription = stringResource(R.string.cd_add_contact)
                        )
                    }
                )
            }

            onHistoryClick?.let {
                ListItem(
                    onClick = it,
                    title = stringResource(id = R.string.action_show_history),
                    startContainer = {
                        Icon(
                            imageVector = Icons.Rounded.History,
                            contentDescription = stringResource(R.string.cd_history)
                        )
                    },
                )
            }

            onSetContactFavoriteClick?.let {
                ListItem(
                    onClick = { it(!isContactFavorite) },
                    title = stringResource(id = if (isContactFavorite) R.string.action_unset_favorite else R.string.action_set_favorite),
                    startContainer = {
                        Icon(
                            imageVector = if (isContactFavorite) Icons.Rounded.StarBorder else Icons.Rounded.Star,
                            contentDescription = stringResource(R.string.cd_favorite_star)
                        )
                    }
                )
            }

            if (onSetBlockedClick != null && isBlocked != null) {
                ListItem(
                    onClick = { onSetBlockedClick(!isBlocked) },
                    title = stringResource(id = if (isBlocked) R.string.action_unblock_contact else R.string.action_block_contact),
                    startContainer = {
                        Icon(
                            imageVector = Icons.Rounded.Block,
                            contentDescription = stringResource(R.string.cd_block)
                        )
                    }
                )
            }

            onDeleteContactClick?.let {
                ListItem(
                    onClick = it,
                    title = stringResource(id = R.string.action_delete_contact),
                    startContainer = {
                        Icon(
                            imageVector = Icons.Rounded.DeleteOutline,
                            contentDescription = stringResource(R.string.cd_delete)
                        )
                    }
                )
            }
        }
    }
}

@Preview
@Composable
fun CallerPreview() {
    Caller(
        number = "213123",
        name = "Nahum Menahem",
        loadingState = LoadingState.SUCCESS,
        phones = listOf(
            Phone(id = 0, contactId = 1, number = "111", label = "one"),
            Phone(id = 1, contactId = 1, number = "222", label = "Two")
        ),
        accounts = listOf(
            ContactAccount(id = 0, contactId = 1, type = ContactAccount.Type.EMAIL),
            ContactAccount(id = 1, contactId = 1, type = ContactAccount.Type.WHATSAPP)
        )
    )
}
