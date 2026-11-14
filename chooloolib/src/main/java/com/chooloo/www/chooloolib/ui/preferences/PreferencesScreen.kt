package com.chooloo.www.chooloolib.ui.preferences

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.ui.common.rememberExternalActions
import com.chooloo.www.chooloolib.ui.components.ConfirmDialog
import com.chooloo.www.chooloolib.ui.components.MessageToast
import com.chooloo.www.chooloolib.ui.components.ScreenTopBar

@Composable
fun PreferencesScreen(
    showDialerPreferences: Boolean,
    viewModel: PreferencesViewModel = hiltViewModel(),
) {
    val externalActions = rememberExternalActions()
    val bugReportUrl = stringResource(R.string.app_bug_reporting_url)
    val donationUrl = stringResource(R.string.donation_link)
    val message by viewModel.message.collectAsStateWithLifecycle()
    MessageToast(message = message, onShown = viewModel::onMessageShown)
    val settings = viewModel.settings.collectAsStateWithLifecycle().value ?: return
    var isClearConfirming by rememberSaveable { mutableStateOf(false) }

    if (isClearConfirming) {
        ConfirmDialog(
            title = stringResource(R.string.action_clear_recents),
            text = stringResource(R.string.explain_clear_recents),
            confirmLabel = stringResource(R.string.action_delete),
            onConfirm = viewModel::onClearRecentsClick,
            onDismiss = { isClearConfirming = false },
        )
    }

    Column {
        ScreenTopBar(title = stringResource(R.string.settings))
        Preferences(
            defaultPage = settings.defaultPage,
            themeMode = settings.themeMode,
            isGroupRecentsEnabled = settings.isGroupRecentsEnabled,
            isDialpadTonesEnabled = settings.isDialpadTonesEnabled,
            isDialpadVibrateEnabled = settings.isDialpadVibrateEnabled,
            showDialerPreferences = showDialerPreferences,
            onDefaultPage = viewModel::onDefaultPage,
            onThemeMode = viewModel::onThemeMode,
            onIsGroupRecentsEnabled = viewModel::onIsGroupRecentsEnabled,
            onIsDialpadTonesEnabled = viewModel::onIsDialpadTonesEnabled,
            onIsDialpadVibrateEnabled = viewModel::onIsDialpadVibrateEnabled,
            onManageBlockedClick = externalActions::openManageBlockedNumbers,
            onClearRecentsClick = { isClearConfirming = true },
            onRateClick = externalActions::openStoreListing,
            onEmailClick = externalActions::sendSupportEmail,
            onReportBugsClick = { externalActions.openUrl(bugReportUrl) },
            onDonateClick = { externalActions.openUrl(donationUrl) },
        )
    }
}
