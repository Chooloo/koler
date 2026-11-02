package com.chooloo.www.chooloolib.ui.contacts

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.domain.model.Contact
import com.chooloo.www.chooloolib.ui.common.LoadingState
import com.chooloo.www.chooloolib.ui.components.list.List

private data class ContactRow(val contact: Contact, val isFavorite: Boolean)

@Composable
fun ContactsList(
    modifier: Modifier = Modifier,
    items: List<Contact>,
    withFavorites: Boolean = false,
    onItemClick: (Contact) -> Unit = {},
    onItemLongClick: (Contact) -> Unit = {},
    loadingState: LoadingState = LoadingState.SUCCESS
) {
    val rows = remember(items, withFavorites) {
        val favorites = if (withFavorites) items.filter { it.isFavorite }.map { ContactRow(it, isFavorite = true) } else emptyList()
        favorites + items.map { ContactRow(it, isFavorite = false) }
    }

    List(
        items = rows,
        modifier = modifier,
        loadingState = loadingState,
        key = { row -> "${row.isFavorite}_${row.contact.id}" },
        loadingTitle = R.string.loading_contacts,
        emptyTitle = R.string.no_results_contacts,
        emptyImage = {
            Icon(
                Icons.Rounded.Person,
                stringResource(id = R.string.cd_contact)
            )
        },
        header = { row ->
            if (row.isFavorite) "★" else row.contact.name?.firstOrNull()?.uppercase() ?: "#"
        },
        item = { row ->
            ContactListItem(
                contact = row.contact,
                onClick = { onItemClick(row.contact) },
                onLongClick = { onItemLongClick(row.contact) }
            )
        }
    )
}

@Preview
@Composable
fun ContactsListPreview() {
    ContactsList(
        items = listOf(
            Contact(id = 0, name = "Nahum Test"),
            Contact(name = "test", id = 1)
        ),
    )
}
