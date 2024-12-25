package com.chooloo.www.koler.ui.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.ExperimentalMotionApi
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.chooloo.www.chooloolib.ui.compose.BottomSheet
import com.chooloo.www.chooloolib.ui.compose.SearchBar
import com.chooloo.www.chooloolib.ui.compose.Tabs
import com.chooloo.www.chooloolib.ui.compose.list.RecentsList
import com.chooloo.www.chooloolib.ui.view.CallerView
import com.chooloo.www.chooloolib.ui.view.ContactsView
import com.chooloo.www.chooloolib.ui.view.DialerView
import com.chooloo.www.chooloolib.ui.view.RecentsView
import com.chooloo.www.chooloolib.ui.viewmodel.preferences.ChoolooPreferencesViewModelImpl
import com.chooloo.www.koler.R
import com.chooloo.www.koler.viewmodel.main.MainViewModelImpl
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class, ExperimentalMotionApi::class)
@Composable
fun KolerView(
    mainViewModel: MainViewModelImpl = hiltViewModel(),
    settingsViewModel: ChoolooPreferencesViewModelImpl = hiltViewModel()
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val pagerState = rememberPagerState(pageCount = { 3 })
    val uiState by mainViewModel.uiState.collectAsState()
    val tabsHeaders =
        listOf(
            stringResource(R.string.dialer),
            stringResource(R.string.contacts),
            stringResource(R.string.recents)
        )

    val motionScene = remember {
        context.resources.openRawResource(R.raw.main_motion_scene).readBytes().decodeToString()
    }

    fun onTextChange(text: String) {
        coroutineScope.launch {
            mainViewModel.onSearchTextChange(text)
        }
    }

    BottomSheet(
        visible = uiState.isMenuVisible,
        onDismiss = { mainViewModel.onDismissMenu() }
    ) {
        KolerPreferencesView(settingsViewModel)
    }

    BottomSheet(
        visible = uiState.selectedRecentData != null,
        onDismiss = mainViewModel::onDismissSelectedRecentData
    ) {
        val recentItem = uiState.selectedRecentData!!
        if (recentItem.groupAccounts.isNotEmpty()) {
            RecentsList(
                items = recentItem.groupAccounts,
                onItemClick = mainViewModel::onRecentDataClick
            )
        } else {
            CallerView(recentId = recentItem.id)
        }
    }

    BottomSheet(
        visible = uiState.selectedContactData != null,
        onDismiss = mainViewModel::onDismissSelectedContactData
    ) {
        CallerView(contactId = uiState.selectedContactData?.id)
    }

//    MotionLayout(
//        modifier = Modifier.fillMaxSize(),
//        motionScene = MotionScene(content = motionScene),
//        debug = EnumSet.of(MotionLayoutDebugFlags.SHOW_ALL),
//    ) {
    ConstraintLayout(
        modifier = Modifier.fillMaxSize(),
        constraintSet = ConstraintSet(motionScene)
    ) {
        Tabs(
            modifier = Modifier
                .layoutId("tabs")
                .padding(
                    PaddingValues(dimensionResource(id = com.chooloo.www.chooloolib.R.dimen.spacing))
                ),
            headers = tabsHeaders,
            selectedTabIndex = pagerState.currentPage,
            onTabClick = { index, _ ->
                coroutineScope.launch {
                    pagerState.scrollToPage(index)
                }
            }
        )

        IconButton(
            modifier = Modifier.layoutId("menu_btn"),
            onClick = { mainViewModel.onMenuButtonClick() }
        ) {
            Icon(imageVector = Icons.Rounded.Menu, contentDescription = "")
        }

        AnimatedVisibility(
            visible = pagerState.currentPage != 0,
            modifier = Modifier.layoutId("search_bar")
        ) {
            SearchBar(
                text = uiState.searchText,
                onTextChange = { onTextChange(it) },
                modifier = Modifier
                    .onFocusChanged { mainViewModel.onSearchFocusChanged(it.isFocused) },
            )
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .layoutId("pager")
        ) { page ->
            when (page) {
                0 -> DialerView()

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