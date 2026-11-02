package com.chooloo.www.chooloolib.ui.contacts

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.domain.model.Contact
import com.chooloo.www.chooloolib.ui.common.LoadingState
import com.chooloo.www.chooloolib.ui.components.list.ItemList

private data class ContactRow(val contact: Contact, val isFavorite: Boolean)

@Composable
fun ContactsList(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    items: List<Contact>,
    withFavorites: Boolean = false,
    onItemClick: (Contact) -> Unit,
    onItemLongClick: ((Contact) -> Unit)?,
    loadingState: LoadingState
) {
    val rows = remember(items, withFavorites) {
        val favorites = if (withFavorites) items.filter { it.isFavorite }.map { ContactRow(it, isFavorite = true) } else emptyList()
        favorites + items.map { ContactRow(it, isFavorite = false) }
    }

    ItemList(
        items = rows,
        modifier = modifier,
        contentPadding = contentPadding,
        loadingState = loadingState,
        key = { row -> "${row.isFavorite}_${row.contact.id}" },
        loadingTitle = R.string.loading_contacts,
        emptyTitle = R.string.no_results_contacts,
        emptyIcon = Icons.Rounded.Person,
        header = { row ->
            if (row.isFavorite) "★" else row.contact.name?.firstOrNull()?.uppercase() ?: "#"
        },
        item = { row ->
            ContactListItem(
                contact = row.contact,
                onClick = { onItemClick(row.contact) },
                onLongClick = onItemLongClick?.let { { it(row.contact) } }
            )
        }
    )
}
