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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.ui.caller.CallerScreen
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
    val coroutineScope = rememberCoroutineScope()
    val uiState by mainViewModel.uiState.collectAsStateWithLifecycle()
    val pagerState = rememberPagerState(
        initialPage = uiState.initialPage,
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
        visible = uiState.selectedRecentData != null,
        onDismiss = mainViewModel::onDismissSelectedRecentData
    ) {
        uiState.selectedRecentData?.let { recentItem ->
            if (recentItem.groupAccounts.isNotEmpty()) {
                RecentsList(
                    items = recentItem.groupAccounts,
                    onItemClick = mainViewModel::onRecentDataClick
                )
            } else {
                CallerScreen(recentId = recentItem.id)
            }
        }
    }

    BottomSheet(
        visible = uiState.selectedContactData != null,
        onDismiss = mainViewModel::onDismissSelectedContactData
    ) {
        CallerScreen(contactId = uiState.selectedContactData?.id)
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
            onSearchTextChange = mainViewModel::onSearchTextChange,
            onSearchFocusChange = mainViewModel::onSearchFocusChanged
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
                    onItemClick = mainViewModel::onContactDataClick
                )

                2 -> RecentsScreen(
                    filter = uiState.searchText,
                    modifier = Modifier.fillMaxSize(),
                    onItemClick = mainViewModel::onRecentDataClick,
                    onItemLongClick = mainViewModel::onRecentDataLongClick
                )
            }
        }
    }
}

@Preview
@Composable
fun MainPreview() {
    MainScreen(
        mainViewModel = viewModel(),
        settingsViewModel = viewModel()
    )
}
