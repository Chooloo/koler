package com.chooloo.www.chooloolib.ui.viewmodel.preferences

import com.chooloo.www.chooloolib.domain.repository.preference.PreferenceRepository
import com.chooloo.www.chooloolib.domain.repository.preference.PreferenceRepository.Companion.IncomingCallMode
import com.chooloo.www.chooloolib.domain.repository.preference.PreferenceRepository.Companion.Page
import com.chooloo.www.chooloolib.ui.viewmodel.base.BaseViewModelImpl
import com.chooloo.www.chooloolib.ui.viewmodel.preferences.ChoolooPreferencesViewModel.Companion.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ChoolooPreferencesViewModelImpl @Inject constructor(
    private val preferenceRepository: PreferenceRepository
) : BaseViewModelImpl(), ChoolooPreferencesViewModel {
    private val _uiState = MutableStateFlow(UiState())

    override val uiState: StateFlow<UiState> = _uiState.asStateFlow()


    override fun onDefaultPage(page: Page) {
        preferenceRepository.setDefaultPage(page)
    }

    override fun onIsDialpadTonesEnabled(toggled: Boolean) {
        preferenceRepository.setIsDialpadTonesEnabled(toggled)
    }

    override fun onIsGroupRecentsEnabled(toggled: Boolean) {
        preferenceRepository.setIsGroupRecentsEnabled(toggled)
    }

    override fun onIsDialpadVibrateEnabled(toggled: Boolean) {
        preferenceRepository.setIsDialpadVibrateEnabled(toggled)
    }

    override fun onIncomingCallMode(callMode: IncomingCallMode) {
        preferenceRepository.setIncomingCallModel(callMode)
    }
}