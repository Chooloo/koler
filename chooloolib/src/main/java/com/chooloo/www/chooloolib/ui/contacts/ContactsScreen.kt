package com.chooloo.www.chooloolib.ui.contacts

import android.Manifest
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.chooloo.www.chooloolib.domain.model.Contact
import com.chooloo.www.chooloolib.ui.components.Permissioned

@Composable
fun ContactsScreen(
    modifier: Modifier = Modifier,
    filter: String,
    withFavorites: Boolean = true,
    onItemClick: (Contact) -> Unit = {},
    onItemLongClick: (Contact) -> Unit = {},
    viewModel: ContactsViewModel = hiltViewModel()
) {

    Permissioned(
        permissions = listOf(
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.WRITE_CONTACTS
        )
    ) {
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
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
