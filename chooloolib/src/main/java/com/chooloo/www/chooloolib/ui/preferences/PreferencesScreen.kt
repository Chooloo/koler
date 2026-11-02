package com.chooloo.www.chooloolib.ui.preferences

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.ui.common.openManageBlockedNumbers
import com.chooloo.www.chooloolib.ui.common.openStoreListing
import com.chooloo.www.chooloolib.ui.common.openUrl
import com.chooloo.www.chooloolib.ui.common.sendSupportEmail

@Composable
fun PreferencesScreen(
    showDialerPreferences: Boolean = true,
    viewModel: PreferencesViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Preferences(
        defaultPage = uiState.defaultPage,
        themeMode = uiState.themeMode,
        isAnimationsEnabled = uiState.isAnimationsEnabled,
        incomingCallMode = uiState.incomingCallMode,
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
        onManageBlockedClick = context::openManageBlockedNumbers,
        onClearRecentsClick = viewModel::onClearRecentsClick,
        onRateClick = context::openStoreListing,
        onEmailClick = context::sendSupportEmail,
        onReportBugsClick = { context.openUrl(context.getString(R.string.app_bug_reporting_url)) },
        onDonateClick = { context.openUrl(context.getString(R.string.donation_link)) },
    )
}
