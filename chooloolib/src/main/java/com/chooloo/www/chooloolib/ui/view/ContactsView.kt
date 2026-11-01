package com.chooloo.www.chooloolib.ui.view

import android.Manifest
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.chooloo.www.chooloolib.domain.model.ContactData
import com.chooloo.www.chooloolib.ui.compose.Permissioned
import com.chooloo.www.chooloolib.ui.compose.list.ContactsList
import com.chooloo.www.chooloolib.ui.viewmodel.contacts.ContactsViewModel

@Composable
fun ContactsView(
    modifier: Modifier = Modifier,
    filter: String,
    withFavorites: Boolean = true,
    onItemClick: (ContactData) -> Unit = {},
    onItemLongClick: (ContactData) -> Unit = {},
    viewModel: ContactsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Permissioned(
        permissions = listOf(
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.WRITE_CONTACTS
        )
    ) {
        LaunchedEffect(key1 = filter) {
            viewModel.onFilterChanged(filter)
        }
        ContactsList(
            modifier = modifier,
            items = uiState.items,
            withFavorites = withFavorites && filter.isEmpty(),
            onItemClick = onItemClick,
            onItemLongClick = onItemLongClick,
            loadingState = uiState.loadingState,
        )
    }
}
