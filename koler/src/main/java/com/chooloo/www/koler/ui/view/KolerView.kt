package com.chooloo.www.koler.ui.view

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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.ui.compose.AppBarHeader
import com.chooloo.www.chooloolib.ui.compose.BottomSheet
import com.chooloo.www.chooloolib.ui.compose.list.RecentsList
import com.chooloo.www.chooloolib.ui.view.CallerView
import com.chooloo.www.chooloolib.ui.view.ChoolooPreferencesView
import com.chooloo.www.chooloolib.ui.view.ContactsView
import com.chooloo.www.chooloolib.ui.view.DialerView
import com.chooloo.www.chooloolib.ui.view.RecentsView
import com.chooloo.www.chooloolib.ui.viewmodel.preferences.ChoolooPreferencesViewModel
import com.chooloo.www.koler.ui.viewmodel.MainViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun KolerView(
    mainViewModel: MainViewModel = hiltViewModel(),
    settingsViewModel: ChoolooPreferencesViewModel = hiltViewModel()
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
        ChoolooPreferencesView(
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
                CallerView(recentId = recentItem.id)
            }
        }
    }

    BottomSheet(
        visible = uiState.selectedContactData != null,
        onDismiss = mainViewModel::onDismissSelectedContactData
    ) {
        CallerView(contactId = uiState.selectedContactData?.id)
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
                0 -> DialerView(initialNumber = uiState.dialNumber)

                1 -> ContactsView(
                    filter = uiState.searchText,
                    modifier = Modifier.fillMaxSize(),
                    onItemClick = mainViewModel::onContactDataClick
                )

                2 -> RecentsView(
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
    KolerView(
        mainViewModel = viewModel(),
        settingsViewModel = viewModel()
    )
}
