package com.chooloo.www.chooloolib.ui.view

import android.Manifest
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.chooloo.www.chooloolib.domain.model.ContactData
import com.chooloo.www.chooloolib.ui.compose.Permissioned
import com.chooloo.www.chooloolib.ui.compose.list.ContactsList
import com.chooloo.www.chooloolib.ui.viewmodel.contacts.ContactsViewModelImpl

@Composable
fun ContactsView(
    modifier: Modifier = Modifier,
    filter: String,
    onItemClick: (ContactData) -> Unit = {},
    onItemLongClick: (ContactData) -> Unit = {},
    viewModel: ContactsViewModelImpl = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(key1 = filter) {
        viewModel.onFilterChanged(filter)
    }

    Permissioned(
        permissions = listOf(
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.WRITE_CONTACTS
        )
    ) {
        ContactsList(
            modifier = modifier,
            items = uiState.items,
            onItemClick = onItemClick,
            onItemLongClick = onItemLongClick,
            loadingState = uiState.loadingState,
        )
    }
}