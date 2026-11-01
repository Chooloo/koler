package com.chooloo.www.chooloolib.ui.viewmodel.preferences

import androidx.lifecycle.viewModelScope
import com.chooloo.www.chooloolib.domain.model.ThemeMode
import com.chooloo.www.chooloolib.domain.repository.navigation.NavigationRepository
import com.chooloo.www.chooloolib.domain.repository.preference.PreferenceRepository
import com.chooloo.www.chooloolib.domain.repository.preference.PreferenceRepository.Companion.IncomingCallMode
import com.chooloo.www.chooloolib.domain.repository.preference.PreferenceRepository.Companion.Page
import com.chooloo.www.chooloolib.domain.repository.recent.RecentRepository
import com.chooloo.www.chooloolib.ui.viewmodel.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChoolooPreferencesViewModel @Inject constructor(
    private val recentRepository: RecentRepository,
    private val navigationRepository: NavigationRepository,
    private val preferenceRepository: PreferenceRepository,
) : BaseViewModel() {
    data class UiState(
        val defaultPage: Page? = null,
        val themeMode: ThemeMode? = null,
        val isAnimationsEnabled: Boolean? = null,
        val isDialpadTonesEnabled: Boolean? = null,
        val isGroupRecentsEnabled: Boolean? = null,
        val isDialpadVibrateEnabled: Boolean? = null,
        val incomingCallModel: IncomingCallMode? = null
    )

    val uiState: StateFlow<UiState> = combine(
        preferenceRepository.defaultPage,
        preferenceRepository.themeMode,
        preferenceRepository.isAnimationsEnabled,
        preferenceRepository.isDialpadTonesEnabled,
        preferenceRepository.isGroupRecentsEnabled,
        preferenceRepository.isDialpadVibrateEnabled,
        preferenceRepository.incomingCallMode
    ) { values ->
        UiState(
            defaultPage = values[0] as Page,
            themeMode = values[1] as ThemeMode,
            isAnimationsEnabled = values[2] as Boolean,
            isDialpadTonesEnabled = values[3] as Boolean,
            isGroupRecentsEnabled = values[4] as Boolean,
            isDialpadVibrateEnabled = values[5] as Boolean,
            incomingCallModel = values[6] as IncomingCallMode
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        UiState(
            defaultPage = preferenceRepository.defaultPage.value,
            themeMode = preferenceRepository.themeMode.value,
            isAnimationsEnabled = preferenceRepository.isAnimationsEnabled.value,
            isDialpadTonesEnabled = preferenceRepository.isDialpadTonesEnabled.value,
            isGroupRecentsEnabled = preferenceRepository.isGroupRecentsEnabled.value,
            isDialpadVibrateEnabled = preferenceRepository.isDialpadVibrateEnabled.value,
            incomingCallModel = preferenceRepository.incomingCallMode.value
        )
    )

    fun onDefaultPage(page: Page) {
        preferenceRepository.setDefaultPage(page)
    }

    fun onThemeMode(themeMode: ThemeMode) {
        preferenceRepository.setThemeMode(themeMode)
    }

    fun onIsAnimationsEnabled(toggled: Boolean) {
        preferenceRepository.setIsAnimationsEnabled(toggled)
    }

    fun onIsDialpadTonesEnabled(toggled: Boolean) {
        preferenceRepository.setIsDialpadTonesEnabled(toggled)
    }

    fun onIsGroupRecentsEnabled(toggled: Boolean) {
        preferenceRepository.setIsGroupRecentsEnabled(toggled)
    }

    fun onIsDialpadVibrateEnabled(toggled: Boolean) {
        preferenceRepository.setIsDialpadVibrateEnabled(toggled)
    }

    fun onIncomingCallMode(callMode: IncomingCallMode) {
        preferenceRepository.setIncomingCallModel(callMode)
    }

    fun onManageBlockedClick() {
        viewModelScope.launch {
            navigationRepository.goToManageBlockedNumbers()
        }
    }

    fun onClearRecentsClick() {
        viewModelScope.launch {
            try {
                recentRepository.deleteAllRecents()
            } catch (_: Exception) {
            }
        }
    }

    fun onRateClick() {
        viewModelScope.launch {
            navigationRepository.goToRateApp()
        }
    }

    fun onEmailClick() {
        viewModelScope.launch {
            navigationRepository.goToSendEmail()
        }
    }

    fun onReportBugsClick() {
        viewModelScope.launch {
            navigationRepository.goToReportBug()
        }
    }

    fun onDonateClick() {
        viewModelScope.launch {
            navigationRepository.goToDonate()
        }
    }
}
