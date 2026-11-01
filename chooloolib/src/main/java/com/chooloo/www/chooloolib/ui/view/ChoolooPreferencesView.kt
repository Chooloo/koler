package com.chooloo.www.chooloolib.ui.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.chooloo.www.chooloolib.ui.compose.ChoolooPreferences
import com.chooloo.www.chooloolib.ui.viewmodel.preferences.ChoolooPreferencesViewModel

@Composable
fun ChoolooPreferencesView(
    showDialerPreferences: Boolean = true,
    viewModel: ChoolooPreferencesViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ChoolooPreferences(
        defaultPage = uiState.defaultPage,
        themeMode = uiState.themeMode,
        isAnimationsEnabled = uiState.isAnimationsEnabled,
        incomingCallMode = uiState.incomingCallModel,
        isGroupRecentsEnabled = uiState.isGroupRecentsEnabled,
        isDialpadTonesEnabled = uiState.isDialpadTonesEnabled,
        isDialpadVibrateEnabled = uiState.isDialpadVibrateEnabled,
        showDialerPreferences = showDialerPreferences,
        onDefaultPage = viewModel::onDefaultPage,
        onThemeMode = viewModel::onThemeMode,
        onIsAnimationsEnabled = viewModel::onIsAnimationsEnabled,
        onIncomingCallMode = viewModel::onIncomingCallMode,
        onIsGroupRecentsEnabled = viewModel::onIsGroupRecentsEnabled,
        onIsDialpadTonesEnabled = viewModel::onIsDialpadTonesEnabled,
        onIsDialpadVibrateEnabled = viewModel::onIsDialpadVibrateEnabled,
        onManageBlockedClick = viewModel::onManageBlockedClick,
        onClearRecentsClick = viewModel::onClearRecentsClick,
        onRateClick = viewModel::onRateClick,
        onEmailClick = viewModel::onEmailClick,
        onReportBugsClick = viewModel::onReportBugsClick,
        onDonateClick = viewModel::onDonateClick,
    )
}
