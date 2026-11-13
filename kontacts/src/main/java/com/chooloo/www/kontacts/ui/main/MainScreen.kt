package com.chooloo.www.kontacts.ui.main

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PersonAdd
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.ui.caller.CallerScreen
import com.chooloo.www.chooloolib.ui.common.createContact
import com.chooloo.www.chooloolib.ui.components.BottomSheet
import com.chooloo.www.chooloolib.ui.components.FloatingBubble
import com.chooloo.www.chooloolib.ui.contacts.ContactsScreen
import com.chooloo.www.chooloolib.ui.home.HomeScaffold
import com.chooloo.www.chooloolib.ui.preferences.PreferencesScreen

@Composable
fun MainScreen(
    viewModel: MainViewModel,
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    HomeScaffold(
        title = stringResource(R.string.contacts),
        isSearching = uiState.isSearching,
        searchText = uiState.searchText,
        searchHintRes = R.string.hint_search_contacts,
        onSearchOpen = viewModel::onSearchOpen,
        onSearchTextChange = viewModel::onSearchTextChange,
        onSearchClose = viewModel::onSearchClose,
        onSettingsOpen = viewModel::onSettingsOpen,
        bottomBarContent = {
            FloatingBubble(
                icon = Icons.Rounded.PersonAdd,
                contentDescription = stringResource(R.string.cd_add_contact),
                onClick = { context.createContact() },
                modifier = Modifier.align(Alignment.CenterStart),
            )
        },
    ) { listPadding ->
        ContactsScreen(
            filter = uiState.searchText,
            contentPadding = listPadding,
            onItemClick = { viewModel.onContactSelected(it.id) },
        )
    }

    BottomSheet(visible = uiState.isSettingsOpen, skipPartiallyExpanded = true, onDismiss = viewModel::onSettingsClose) {
        PreferencesScreen(showDialerPreferences = false)
    }

    BottomSheet(visible = uiState.selectedCaller != null, onDismiss = viewModel::onContactDismiss) {
        uiState.selectedCaller?.let { CallerScreen(target = it, onDismiss = viewModel::onContactDismiss) }
    }
}
