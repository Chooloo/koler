package com.chooloo.www.koler.ui.main

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
import com.chooloo.www.chooloolib.ui.common.copyToClipboard
import com.chooloo.www.chooloolib.ui.common.createContact
import com.chooloo.www.chooloolib.ui.components.BottomSheet
import com.chooloo.www.chooloolib.ui.components.FloatingBottomBar
import com.chooloo.www.chooloolib.ui.components.FloatingBubble
import com.chooloo.www.chooloolib.ui.components.Reveal
import com.chooloo.www.chooloolib.ui.components.ScreenTopBar
import com.chooloo.www.chooloolib.ui.contacts.ContactsScreen
import com.chooloo.www.chooloolib.ui.dialpad.DialpadScreen
import com.chooloo.www.chooloolib.ui.preferences.PreferencesScreen
import com.chooloo.www.chooloolib.ui.recents.RecentsScreen
import com.chooloo.www.chooloolib.ui.theme.Sizes
import com.chooloo.www.koler.ui.main.MainViewModel.Tab

@Composable
fun MainScreen(
    viewModel: MainViewModel,
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val tab = uiState.tab ?: return
    val listPadding = PaddingValues(bottom = Sizes.FloatingBarInset)

    DefaultDialerRequest()

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            ScreenTopBar(
                title = stringResource(if (tab == Tab.RECENTS) R.string.recents else R.string.contacts),
                actions = {
                    IconButton(onClick = viewModel::onSettingsOpen) {
                        Icon(Icons.Rounded.Settings, stringResource(R.string.settings))
                    }
                }
            )
            when (tab) {
                Tab.RECENTS -> RecentsScreen(
                    filter = uiState.searchText,
                    contentPadding = listPadding,
                    onItemClick = { viewModel.onCallerSelected(CallerTarget.RecentTarget(it.latest.id)) },
                    onItemLongClick = { context.copyToClipboard(it.latest.number) },
                )
                Tab.CONTACTS -> ContactsScreen(
                    filter = uiState.searchText,
                    contentPadding = listPadding,
                    onItemClick = { viewModel.onCallerSelected(CallerTarget.ContactTarget(it.id)) },
                )
            }
        }

        FloatingBottomBar(
            isSearching = uiState.isSearching,
            searchText = uiState.searchText,
            searchHintRes = if (tab == Tab.RECENTS) R.string.hint_search_recents else R.string.hint_search_contacts,
            onSearchOpen = viewModel::onSearchOpen,
            onSearchTextChange = viewModel::onSearchTextChange,
            onSearchClose = viewModel::onSearchClose,
            modifier = Modifier.align(Alignment.BottomCenter),
        ) {
            Reveal(visible = tab == Tab.CONTACTS, modifier = Modifier.align(Alignment.CenterStart)) {
                FloatingBubble(
                    icon = Icons.Rounded.PersonAdd,
                    contentDescription = stringResource(R.string.cd_add_contact),
                    onClick = { context.createContact() },
                )
            }
            MainNavigationBar(
                selectedTab = tab,
                onTabSelected = viewModel::onTabSelected,
                onDialpadClick = viewModel::onDialpadOpen,
                modifier = Modifier.align(Alignment.Center),
            )
        }
    }

    BottomSheet(visible = uiState.isSettingsOpen, skipPartiallyExpanded = true, onDismiss = viewModel::onSettingsClose) {
        PreferencesScreen(showDialerPreferences = true)
    }

    BottomSheet(visible = uiState.isDialpadOpen, skipPartiallyExpanded = true, onDismiss = viewModel::onDialpadClose) {
        DialpadScreen(initialNumber = uiState.dialNumber)
    }

    BottomSheet(visible = uiState.selectedCaller != null, onDismiss = viewModel::onCallerDismiss) {
        uiState.selectedCaller?.let { CallerScreen(target = it, onDismiss = viewModel::onCallerDismiss) }
    }
}
