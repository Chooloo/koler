package com.chooloo.www.chooloolib.ui.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import coil.compose.AsyncImagePainter
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.domain.model.AccountData
import com.chooloo.www.chooloolib.domain.model.PhoneData
import com.chooloo.www.chooloolib.ui.compose.list.AccountsList
import com.chooloo.www.chooloolib.ui.compose.list.PhonesList
import com.chooloo.www.chooloolib.ui.compose.list.item.ListItem
import com.chooloo.www.chooloolib.utils.LoadingState


@Composable
fun Caller(
    name: String? = null,
    number: String? = null,
    isBlocked: Boolean? = null,
    phones: List<PhoneData> = listOf(),
    imagePainter: AsyncImagePainter? = null,
    accountDatas: List<AccountData> = listOf(),
    loadingState: LoadingState = LoadingState.IDLE,
    phonesLoadingState: LoadingState = LoadingState.IDLE,
    accountsLoadingState: LoadingState = LoadingState.IDLE,
    onSmsClick: (() -> Unit)? = null,
    onCallClick: (() -> Unit)? = null,
    onHistoryClick: (() -> Unit)? = null,
    onPhoneClick: (PhoneData) -> Unit = {},
    onAccountClick: (AccountData) -> Unit = {},
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

    Row {
        imagePainter?.let { CircleImage(painter = imagePainter) }
        Column {
            Text(
                text = name ?: number ?: stringResource(R.string.caller_unknown),
                style = typography.titleLarge
            )
            if (name != null && number != null) {
                Text(text = number, style = typography.labelLarge)
            }
        }
    }

    Row {
        Button(
            onClick = onCallClick ?: {},
            enabled = onCallClick != null
        ) {
            Icon(Icons.Rounded.Call, stringResource(id = R.string.cd_call))
            Text(stringResource(R.string.action_call))
        }

        Button(
            onClick = onSmsClick ?: {},
            enabled = onSmsClick != null
        ) {
            Icon(Icons.Rounded.Sms, stringResource(id = R.string.cd_sms))
            Text(stringResource(R.string.action_sms))
        }

        Button(
            onClick = onEditContactClick ?: {},
            enabled = onEditContactClick != null
        ) {
            Icon(Icons.Rounded.Edit, stringResource(id = R.string.cd_edit))
            Text(stringResource(R.string.action_edit))
        }
    }

    if (phones.isNotEmpty()) {
        Text(
            text = stringResource(id = R.string.phones),
            style = typography.labelSmall
        )
        PhonesList(
            items = phones,
            onItemActionClick = onPhoneClick,
            loadingState = phonesLoadingState
        )
    }

    if (accountDatas.isNotEmpty()) {
        Text(
            style = typography.labelSmall,
            text = stringResource(id = R.string.accounts),
        )
        AccountsList(
            items = accountDatas,
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
            ListItem(onClick = { it(!isContactFavorite) },
                title = stringResource(id = if (isContactFavorite) R.string.action_unset_favorite else R.string.action_set_favorite),
                startContainer = {
                    Icon(
                        imageVector = if (isContactFavorite) Icons.Rounded.StarBorder else Icons.Rounded.Star,
                        contentDescription = stringResource(R.string.cd_favorite_star)
                    )
                })
        }

        if (onSetBlockedClick != null && isBlocked != null) {
            ListItem(onClick = { onSetBlockedClick(!isBlocked) },
                title = stringResource(id = if (isBlocked) R.string.action_unblock_contact else R.string.action_block_contact),
                startContainer = {
                    Icon(
                        imageVector = Icons.Rounded.Block,
                        contentDescription = stringResource(R.string.cd_block)
                    )
                })
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