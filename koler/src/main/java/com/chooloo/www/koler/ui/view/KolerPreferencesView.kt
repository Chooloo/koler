package com.chooloo.www.koler.ui.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.chooloo.www.chooloolib.ui.viewmodel.preferences.ChoolooPreferencesViewModelImpl
import com.chooloo.www.koler.ui.compose.KolerPreferences

@Composable
fun KolerPreferencesView(viewModel: ChoolooPreferencesViewModelImpl = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    KolerPreferences(
        defaultPage = uiState.defaultPage,
        incomingCallMode = uiState.incomingCallModel,
        isGroupRecentsEnabled = uiState.isGroupRecentsEnabled,
        isDialpadTonesEnabled = uiState.isDialpadTonesEnabled,
        isDialpadVibrateEnabled = uiState.isDialpadVibrateEnabled,
        onSelectedDefaultPage = viewModel::onDefaultPage,
        onToggledDialpadTones = viewModel::onIsDialpadTonesEnabled,
        onToggledGroupRecents = viewModel::onIsGroupRecentsEnabled,
        onToggledDialpadVibrate = viewModel::onIsDialpadVibrateEnabled,
        onSelectedIncomingCallMode = viewModel::onIncomingCallMode,
    )
}