package com.chooloo.www.kontacts.ui.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PersonAdd
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.ui.caller.CallerScreen
import com.chooloo.www.chooloolib.ui.caller.CallerTarget
import com.chooloo.www.chooloolib.ui.common.createContact
import com.chooloo.www.chooloolib.ui.components.BottomSheet
import com.chooloo.www.chooloolib.ui.components.FloatingBottomBar
import com.chooloo.www.chooloolib.ui.components.FloatingBubble
import com.chooloo.www.chooloolib.ui.components.ScreenTopBar
import com.chooloo.www.chooloolib.ui.contacts.ContactsScreen
import com.chooloo.www.chooloolib.ui.preferences.PreferencesScreen
import com.chooloo.www.chooloolib.ui.theme.Sizes

@Composable
fun MainScreen(
    viewModel: MainViewModel,
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            ScreenTopBar(
                title = stringResource(R.string.contacts),
                actions = {
                    IconButton(onClick = viewModel::onSettingsOpen) {
                        Icon(Icons.Rounded.Settings, stringResource(R.string.settings))
                    }
                }
            )
            ContactsScreen(
                filter = uiState.searchText,
                contentPadding = PaddingValues(bottom = Sizes.FloatingBarInset),
                onItemClick = { viewModel.onContactSelected(it.id) },
            )
        }

        FloatingBottomBar(
            isSearching = uiState.isSearching,
            searchText = uiState.searchText,
            searchHintRes = R.string.hint_search_contacts,
            onSearchOpen = viewModel::onSearchOpen,
            onSearchTextChange = viewModel::onSearchTextChange,
            onSearchClose = viewModel::onSearchClose,
            modifier = Modifier.align(Alignment.BottomCenter),
        ) {
            FloatingBubble(
                icon = Icons.Rounded.PersonAdd,
                contentDescription = stringResource(R.string.cd_add_contact),
                onClick = { context.createContact() },
                modifier = Modifier.align(Alignment.CenterStart),
            )
        }
    }

    BottomSheet(visible = uiState.isSettingsOpen, skipPartiallyExpanded = true, onDismiss = viewModel::onSettingsClose) {
        PreferencesScreen(showDialerPreferences = false)
    }

    BottomSheet(visible = uiState.selectedContactId != null, onDismiss = viewModel::onContactDismiss) {
        uiState.selectedContactId?.let { CallerScreen(target = CallerTarget.ContactTarget(it), onDismiss = viewModel::onContactDismiss) }
    }
}
