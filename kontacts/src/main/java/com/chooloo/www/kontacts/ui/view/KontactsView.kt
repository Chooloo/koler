package com.chooloo.www.kontacts.ui.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PersonAdd
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.ui.compose.AppBarHeader
import com.chooloo.www.chooloolib.ui.compose.BottomSheet
import com.chooloo.www.chooloolib.ui.theme.Spacing
import com.chooloo.www.chooloolib.ui.view.CallerView
import com.chooloo.www.chooloolib.ui.view.ChoolooPreferencesView
import com.chooloo.www.chooloolib.ui.view.ContactsView
import com.chooloo.www.chooloolib.ui.viewmodel.preferences.ChoolooPreferencesViewModel
import com.chooloo.www.kontacts.ui.viewmodel.MainViewModel

@Composable
fun KontactsView(
    mainViewModel: MainViewModel = hiltViewModel(),
    settingsViewModel: ChoolooPreferencesViewModel = hiltViewModel()
) {
    val uiState by mainViewModel.uiState.collectAsStateWithLifecycle()

    BottomSheet(
        visible = uiState.isMenuVisible,
        onDismiss = mainViewModel::onDismissMenu
    ) {
        ChoolooPreferencesView(
            showDialerPreferences = false,
            viewModel = settingsViewModel
        )
    }

    BottomSheet(
        visible = uiState.selectedContactId != null,
        onDismiss = mainViewModel::onDismissSelectedContact
    ) {
        CallerView(contactId = uiState.selectedContactId)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            AppBarHeader(
                headers = listOf(stringResource(R.string.contacts)),
                selectedTabIndex = 0,
                isSearchVisible = true,
                searchText = uiState.searchText,
                searchHintRes = R.string.hint_search_contacts,
                onMenuClick = mainViewModel::onMenuButtonClick,
                onSearchTextChange = mainViewModel::onSearchTextChange,
                onSearchFocusChange = mainViewModel::onSearchFocusChanged
            )

            ContactsView(
                filter = uiState.searchText,
                modifier = Modifier.fillMaxSize(),
                onItemClick = mainViewModel::onContactDataClick
            )
        }

        FloatingActionButton(
            onClick = mainViewModel::onAddContactClick,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(Spacing.ExtraLarge)
        ) {
            Icon(
                imageVector = Icons.Rounded.PersonAdd,
                contentDescription = stringResource(id = R.string.cd_add_contact)
            )
        }
    }
}
