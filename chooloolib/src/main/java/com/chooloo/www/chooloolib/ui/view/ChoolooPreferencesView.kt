package com.chooloo.www.chooloolib.ui.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.chooloo.www.chooloolib.ui.compose.ChoolooPreferences
import com.chooloo.www.chooloolib.ui.viewmodel.preferences.ChoolooPreferencesViewModelImpl

@Composable
fun ChoolooPreferencesView(
    viewModel: ChoolooPreferencesViewModelImpl = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    ChoolooPreferences(
        defaultPage = uiState.defaultPage,
        incomingCallMode = uiState.incomingCallModel,
        isGroupRecentsEnabled = uiState.isGroupRecentsEnabled,
        isDialpadTonesEnabled = uiState.isDialpadTonesEnabled,
        isDialpadVibrateEnabled = uiState.isDialpadVibrateEnabled,
        onDefaultPage = viewModel::onDefaultPage,
        onIncomingCallMode = viewModel::onIncomingCallMode,
        onIsGroupRecentsEnabled = viewModel::onIsGroupRecentsEnabled,
        onIsDialpadTonesEnabled = viewModel::onIsDialpadTonesEnabled,
        onIsDialpadVibrateEnabled = viewModel::onIsDialpadVibrateEnabled,
    )
}