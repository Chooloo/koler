package com.chooloo.www.koler.ui.main

import android.content.Intent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PersonAdd
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.ui.caller.CallerScreen
import com.chooloo.www.chooloolib.ui.caller.CallerTarget
import com.chooloo.www.chooloolib.ui.common.rememberExternalActions
import com.chooloo.www.chooloolib.ui.components.BottomSheet
import com.chooloo.www.chooloolib.ui.components.FloatingBubble
import com.chooloo.www.chooloolib.ui.components.Reveal
import com.chooloo.www.chooloolib.ui.contacts.ContactsScreen
import com.chooloo.www.chooloolib.ui.dialpad.DialpadScreen
import com.chooloo.www.chooloolib.ui.home.HomeScaffold
import com.chooloo.www.chooloolib.ui.preferences.PreferencesScreen
import com.chooloo.www.chooloolib.ui.recents.RecentsScreen
import com.chooloo.www.koler.ui.call.CallActivity
import com.chooloo.www.koler.ui.main.MainViewModel.Tab

@Composable
fun MainScreen(viewModel: MainViewModel) {
    val context = LocalContext.current
    val externalActions = rememberExternalActions()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val ongoingCall by viewModel.ongoingCall.collectAsStateWithLifecycle()
    val tab = uiState.tab ?: return

    DefaultDialerRequest()

    HomeScaffold(
        title = stringResource(if (tab == Tab.RECENTS) R.string.recents else R.string.contacts),
        isSearching = uiState.isSearching,
        searchText = uiState.searchText,
        searchHintRes = if (tab == Tab.RECENTS) {
            R.string.hint_search_recents
        } else {
            R.string.hint_search_contacts
        },
        onSearchOpen = viewModel.search::open,
        onSearchTextChange = viewModel.search::changeText,
        onSearchClose = viewModel.search::close,
        onSettingsOpen = viewModel::onSettingsOpen,
        bottomBarContent = {
            Reveal(
                visible = tab == Tab.CONTACTS,
                modifier = Modifier.align(Alignment.CenterStart),
            ) {
                FloatingBubble(
                    icon = Icons.Rounded.PersonAdd,
                    contentDescription = stringResource(R.string.cd_add_contact),
                    onClick = { externalActions.createContact() },
                )
            }
            MainNavigationBar(
                selectedTab = tab,
                onTabSelected = viewModel::onTabSelected,
                onDialpadClick = viewModel::onDialpadOpen,
                modifier = Modifier.align(Alignment.Center),
            )
        },
    ) { listPadding ->
        ongoingCall?.let {
            OngoingCallBanner(
                ongoingCall = it,
                onClick = { context.startActivity(Intent(context, CallActivity::class.java)) },
            )
        }
        when (tab) {
            Tab.RECENTS -> RecentsScreen(
                filter = uiState.searchText,
                contentPadding = listPadding,
                onItemClick = {
                    viewModel.onCallerSelected(CallerTarget.RecentTarget(it.latest.id))
                },
                onItemLongClick = { externalActions.copyToClipboard(it.latest.number) },
            )

            Tab.CONTACTS -> ContactsScreen(
                filter = uiState.searchText,
                contentPadding = listPadding,
                onItemClick = { viewModel.onCallerSelected(CallerTarget.ContactTarget(it.id)) },
            )
        }
    }

    BottomSheet(
        visible = uiState.isSettingsOpen,
        skipPartiallyExpanded = true,
        onDismiss = viewModel::onSettingsClose,
    ) {
        PreferencesScreen(showDialerPreferences = true)
    }

    BottomSheet(
        visible = uiState.isDialpadOpen,
        skipPartiallyExpanded = true,
        onDismiss = viewModel::onDialpadClose,
    ) {
        DialpadScreen(initialNumber = uiState.dialNumber)
    }

    BottomSheet(visible = uiState.selectedCaller != null, onDismiss = viewModel::onCallerDismiss) {
        uiState.selectedCaller?.let {
            CallerScreen(target = it, onDismiss = viewModel::onCallerDismiss)
        }
    }
}
