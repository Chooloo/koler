package com.chooloo.www.chooloolib.ui.viewmodel.preferences

import com.chooloo.www.chooloolib.domain.repository.preference.PreferenceRepository.Companion.IncomingCallMode
import com.chooloo.www.chooloolib.domain.repository.preference.PreferenceRepository.Companion.Page
import com.chooloo.www.chooloolib.ui.viewmodel.base.BaseViewModel
import kotlinx.coroutines.flow.StateFlow

interface ChoolooPreferencesViewModel : BaseViewModel {
    companion object {
        data class UiState(
            val defaultPage: Page? = null,
            val isDialpadTonesEnabled: Boolean? = null,
            val isGroupRecentsEnabled: Boolean? = null,
            val isDialpadVibrateEnabled: Boolean? = null,
            val incomingCallModel: IncomingCallMode? = null
        )
    }

    val uiState: StateFlow<UiState>

    fun onDefaultPage(page: Page)
    fun onIsDialpadTonesEnabled(toggled: Boolean)
    fun onIsGroupRecentsEnabled(toggled: Boolean)
    fun onIsDialpadVibrateEnabled(toggled: Boolean)
    fun onIncomingCallMode(callMode: IncomingCallMode)
}