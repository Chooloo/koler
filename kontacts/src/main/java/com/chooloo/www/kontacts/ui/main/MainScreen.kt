package com.chooloo.www.kontacts.ui.main

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.ui.caller.CallerScreen
import com.chooloo.www.chooloolib.ui.caller.CallerTarget
import com.chooloo.www.chooloolib.ui.common.createContact
import com.chooloo.www.chooloolib.ui.components.AppBarHeader
import com.chooloo.www.chooloolib.ui.components.BottomSheet
import com.chooloo.www.chooloolib.ui.contacts.ContactsScreen
import com.chooloo.www.chooloolib.ui.preferences.PreferencesScreen
import com.chooloo.www.chooloolib.ui.preferences.PreferencesViewModel
import com.chooloo.www.chooloolib.ui.theme.Spacing

@Composable
fun MainScreen(
    mainViewModel: MainViewModel = hiltViewModel(),
    settingsViewModel: PreferencesViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val uiState by mainViewModel.uiState.collectAsStateWithLifecycle()

    BottomSheet(
        visible = uiState.isMenuVisible,
        onDismiss = mainViewModel::onDismissMenu
    ) {
        PreferencesScreen(
            showDialerPreferences = false,
            viewModel = settingsViewModel
        )
    }

    BottomSheet(
        visible = uiState.selectedContactId != null,
        onDismiss = mainViewModel::onDismissSelectedContact
    ) {
        uiState.selectedContactId?.let { CallerScreen(CallerTarget.ContactTarget(it)) }
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
                onSearchTextChange = mainViewModel::onSearchTextChange
            )

            ContactsScreen(
                filter = uiState.searchText,
                modifier = Modifier.fillMaxSize(),
                onItemClick = mainViewModel::onContactClick
            )
        }

        FloatingActionButton(
            onClick = { context.createContact() },
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
