package com.chooloo.www.chooloolib.ui.caller

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.domain.model.ContactAccount
import com.chooloo.www.chooloolib.ui.components.list.List
import com.chooloo.www.chooloolib.utils.LoadingState

@Composable
fun AccountsList(
    modifier: Modifier = Modifier,
    items: List<ContactAccount>,
    onItemClick: (ContactAccount) -> Unit = {},
    onItemLongClick: (ContactAccount) -> Unit = {},
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
                account = item,
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
            ContactAccount(id = 0, contactId = 0, type = ContactAccount.Type.EMAIL),
            ContactAccount(id = 0, contactId = 0, type = ContactAccount.Type.EMAIL)
        )
    )
}

@Preview
@Composable
fun LoadingAccountsListPreview() {
    AccountsList(
        loadingState = LoadingState.LOADING,
        items = listOf(
            ContactAccount(id = 0, contactId = 0, type = ContactAccount.Type.EMAIL),
            ContactAccount(id = 0, contactId = 0, type = ContactAccount.Type.EMAIL)
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