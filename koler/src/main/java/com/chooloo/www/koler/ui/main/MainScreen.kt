package com.chooloo.www.koler.ui.main

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.ui.caller.CallerScreen
import com.chooloo.www.chooloolib.ui.common.copyToClipboard
import com.chooloo.www.chooloolib.ui.components.AppBarHeader
import com.chooloo.www.chooloolib.ui.components.BottomSheet
import com.chooloo.www.chooloolib.ui.contacts.ContactsScreen
import com.chooloo.www.chooloolib.ui.dialpad.DialpadScreen
import com.chooloo.www.chooloolib.ui.preferences.PreferencesScreen
import com.chooloo.www.chooloolib.ui.preferences.PreferencesViewModel
import com.chooloo.www.chooloolib.ui.recents.RecentsList
import com.chooloo.www.chooloolib.ui.recents.RecentsScreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainScreen(
    mainViewModel: MainViewModel = hiltViewModel(),
    settingsViewModel: PreferencesViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val uiState by mainViewModel.uiState.collectAsStateWithLifecycle()
    val initialPage = uiState.initialPage ?: return
    val pagerState = rememberPagerState(
        initialPage = initialPage.ordinal,
        pageCount = { 3 }
    )
    val tabsHeaders = listOf(
        stringResource(R.string.dialer),
        stringResource(R.string.contacts),
        stringResource(R.string.recents)
    )

    LaunchedEffect(uiState.dialNumber) {
        if (!uiState.dialNumber.isNullOrEmpty()) {
            pagerState.scrollToPage(0)
        }
    }

    DefaultDialerRequest()

    BottomSheet(
        visible = uiState.isMenuVisible,
        onDismiss = { mainViewModel.onDismissMenu() }
    ) {
        PreferencesScreen(
            showDialerPreferences = true,
            viewModel = settingsViewModel
        )
    }

    BottomSheet(
        visible = uiState.selectedRecentGroup != null,
        onDismiss = mainViewModel::onDismissSelectedRecentGroup
    ) {
        uiState.selectedRecentGroup?.let { group ->
            if (group.recents.size > 1) {
                RecentsList(
                    items = group.recents,
                    onItemClick = mainViewModel::onRecentGroupClick
                )
            } else {
                CallerScreen(recentId = group.latest.id)
            }
        }
    }

    BottomSheet(
        visible = uiState.selectedContact != null,
        onDismiss = mainViewModel::onDismissSelectedContact
    ) {
        CallerScreen(contactId = uiState.selectedContact?.id)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        AppBarHeader(
            headers = tabsHeaders,
            selectedTabIndex = pagerState.currentPage,
            isSearchVisible = pagerState.currentPage != 0,
            searchText = uiState.searchText,
            searchHintRes = if (pagerState.currentPage == 1) R.string.hint_search_contacts else R.string.hint_search_recents,
            onTabClick = { index, _ ->
                coroutineScope.launch {
                    pagerState.animateScrollToPage(index)
                }
            },
            onMenuClick = mainViewModel::onMenuButtonClick,
            onSearchTextChange = mainViewModel::onSearchTextChange
        )

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            when (page) {
                0 -> DialpadScreen(initialNumber = uiState.dialNumber)

                1 -> ContactsScreen(
                    filter = uiState.searchText,
                    modifier = Modifier.fillMaxSize(),
                    onItemClick = mainViewModel::onContactClick
                )

                2 -> RecentsScreen(
                    filter = uiState.searchText,
                    modifier = Modifier.fillMaxSize(),
                    onItemClick = mainViewModel::onRecentGroupClick,
                    onItemLongClick = { context.copyToClipboard(it.latest.number) }
                )
            }
        }
    }
}

