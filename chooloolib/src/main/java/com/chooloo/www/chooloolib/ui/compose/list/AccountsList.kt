package com.chooloo.www.chooloolib.ui.compose.list

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.domain.model.AccountData
import com.chooloo.www.chooloolib.domain.model.record.RawContactRecord.RawContactType
import com.chooloo.www.chooloolib.ui.compose.list.base.List
import com.chooloo.www.chooloolib.ui.compose.list.item.AccountListItem
import com.chooloo.www.chooloolib.utils.LoadingState

@Composable
fun AccountsList(
    modifier: Modifier = Modifier,
    items: List<AccountData>,
    onItemClick: (AccountData) -> Unit = {},
    onItemLongClick: (AccountData) -> Unit = {},
    loadingState: LoadingState = LoadingState.IDLE
) {
    List(
        modifier = modifier,
        items = items,
        loadingState = loadingState,
        emptyTitle = R.string.no_results_contacts,
        loadingTitle = R.string.loading_accounts,
        key = { item -> item.hashCode().toString() },
        emptyImage = {
            Icon(Icons.Rounded.Person, stringResource(R.string.cd_accounts))
        },
        item = { item ->
            AccountListItem(
                accountData = item,
                onClick = { onItemClick(item) },
                onLongClick = { onItemLongClick(item) }
            )
        }
    )
}

@Preview
@Composable
fun AccountsListPreview() {
    AccountsList(
        loadingState = LoadingState.SUCCESS,
        items = listOf(
            AccountData(
                contactId = 0,
                type = RawContactType.EMAIL
            ),
            AccountData(
                contactId = 0,
                type = RawContactType.EMAIL
            )
        )
    )
}

@Preview
@Composable
fun LoadingAccountsListPreview() {
    AccountsList(
        loadingState = LoadingState.LOADING,
        items = listOf(
            AccountData(
                contactId = 0,
                type = RawContactType.EMAIL
            ),
            AccountData(
                contactId = 0,
                type = RawContactType.EMAIL
            )
        )
    )
}

@Preview
@Composable
fun EmptyAccountsListPreview() {
    AccountsList(
        items = emptyList(),
        loadingState = LoadingState.IDLE,
    )
}