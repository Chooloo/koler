package com.chooloo.www.chooloolib.ui.compose.list

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.domain.model.ContactData
import com.chooloo.www.chooloolib.ui.compose.list.base.List
import com.chooloo.www.chooloolib.ui.compose.list.item.ContactListItem
import com.chooloo.www.chooloolib.utils.LoadingState

@Composable
fun ContactsList(
    modifier: Modifier = Modifier,
    items: List<ContactData>,
    withFavorites: Boolean = false,
    onItemClick: (ContactData) -> Unit = {},
    onItemLongClick: (ContactData) -> Unit = {},
    loadingState: LoadingState = LoadingState.IDLE
) {
    val favItems = items.filter { it.isFavorite } + items
    val finalItems = if (withFavorites) favItems + items else items

    List(
        items = finalItems,
        modifier = modifier,
        loadingState = loadingState,
        key = { item -> item.id.toString() },
        loadingTitle = R.string.loading_contacts,
        emptyTitle = R.string.no_results_contacts,
        emptyImage = {
            Icon(
                Icons.Rounded.Person,
                stringResource(id = R.string.cd_contact)
            )
        },
        header = { item ->
            if (withFavorites and (finalItems.indexOf(item) < favItems.count())) {
                "★"
            } else {
                item.name?.get(0).toString() ?: ""
            }
        },
        item = { item ->
            ContactListItem(
                contactData = item,
                onClick = { onItemClick(item) },
                onLongClick = { onItemLongClick(item) }
            )
        }
    )
}

@Preview
@Composable
fun ContactsListPreview() {
    ContactsList(
        loadingState = LoadingState.SUCCESS,
        items = listOf(
            ContactData(id = 0, name = "Nahum Test"),
            ContactData(name = "test", id = 0)
        ),
    )
}

@Preview
@Composable
fun LoadingContactsListPreview() {
    ContactsList(
        loadingState = LoadingState.LOADING,
        items = listOf(
            ContactData(id = 0, name = "Nahum Test"),
            ContactData(name = "test", id = 0)
        ),
    )
}

@Preview
@Composable
fun EmptyContactsListPreview() {
    ContactsList(
        items = emptyList(),
        loadingState = LoadingState.IDLE,
    )
}