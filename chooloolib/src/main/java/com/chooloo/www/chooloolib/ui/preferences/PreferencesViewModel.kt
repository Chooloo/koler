package com.chooloo.www.chooloolib.ui.preferences

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chooloo.www.chooloolib.domain.model.IncomingCallMode
import com.chooloo.www.chooloolib.domain.model.Page
import com.chooloo.www.chooloolib.domain.model.ThemeMode
import com.chooloo.www.chooloolib.domain.repository.PreferenceRepository
import com.chooloo.www.chooloolib.domain.repository.RecentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class PreferencesViewModel @Inject constructor(
    private val recentRepository: RecentRepository,
    private val preferenceRepository: PreferenceRepository,
) : ViewModel() {
    data class UiState(
        val defaultPage: Page,
        val themeMode: ThemeMode,
        val isAnimationsEnabled: Boolean,
        val isDialpadTonesEnabled: Boolean,
        val isGroupRecentsEnabled: Boolean,
        val isDialpadVibrateEnabled: Boolean,
        val incomingCallMode: IncomingCallMode,
    )

    private fun currentState() = with(preferenceRepository) {
        UiState(
            defaultPage = defaultPage.value,
            themeMode = themeMode.value,
            isAnimationsEnabled = isAnimationsEnabled.value,
            isDialpadTonesEnabled = isDialpadTonesEnabled.value,
            isGroupRecentsEnabled = isGroupRecentsEnabled.value,
            isDialpadVibrateEnabled = isDialpadVibrateEnabled.value,
            incomingCallMode = incomingCallMode.value,
        )
    }

    val uiState: StateFlow<UiState> = with(preferenceRepository) {
        combine<Any, UiState>(
            defaultPage,
            themeMode,
            isAnimationsEnabled,
            isDialpadTonesEnabled,
            isGroupRecentsEnabled,
            isDialpadVibrateEnabled,
            incomingCallMode,
        ) { currentState() }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), currentState())

    fun onDefaultPage(page: Page) = preferenceRepository.setDefaultPage(page)

    fun onThemeMode(themeMode: ThemeMode) = preferenceRepository.setThemeMode(themeMode)

    fun onIsAnimationsEnabled(toggled: Boolean) = preferenceRepository.setIsAnimationsEnabled(toggled)

    fun onIsDialpadTonesEnabled(toggled: Boolean) = preferenceRepository.setIsDialpadTonesEnabled(toggled)

    fun onIsGroupRecentsEnabled(toggled: Boolean) = preferenceRepository.setIsGroupRecentsEnabled(toggled)

    fun onIsDialpadVibrateEnabled(toggled: Boolean) = preferenceRepository.setIsDialpadVibrateEnabled(toggled)

    fun onIncomingCallMode(callMode: IncomingCallMode) = preferenceRepository.setIncomingCallMode(callMode)

    fun onClearRecentsClick() {
        viewModelScope.launch {
            runCatching { recentRepository.deleteAllRecents() }
                .onFailure { Log.e(TAG, "Failed clearing recents", it) }
        }
    }

    private companion object {
        const val TAG = "PreferencesViewModel"
    }
}
