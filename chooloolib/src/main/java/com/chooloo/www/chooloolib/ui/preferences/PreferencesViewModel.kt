package com.chooloo.www.chooloolib.ui.preferences

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chooloo.www.chooloolib.domain.model.IncomingCallMode
import com.chooloo.www.chooloolib.domain.model.Page
import com.chooloo.www.chooloolib.domain.model.Settings
import com.chooloo.www.chooloolib.domain.model.ThemeMode
import com.chooloo.www.chooloolib.domain.repository.PreferenceRepository
import com.chooloo.www.chooloolib.domain.repository.RecentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class PreferencesViewModel @Inject constructor(
    private val recentRepository: RecentRepository,
    private val preferenceRepository: PreferenceRepository,
) : ViewModel() {
    val settings: StateFlow<Settings?> =
        preferenceRepository.settings.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    fun onDefaultPage(page: Page) = update { setDefaultPage(page) }

    fun onThemeMode(themeMode: ThemeMode) = update { setThemeMode(themeMode) }

    fun onIncomingCallMode(callMode: IncomingCallMode) = update { setIncomingCallMode(callMode) }

    fun onIsAnimationsEnabled(isEnabled: Boolean) = update { setIsAnimationsEnabled(isEnabled) }

    fun onIsGroupRecentsEnabled(isEnabled: Boolean) = update { setIsGroupRecentsEnabled(isEnabled) }

    fun onIsDialpadTonesEnabled(isEnabled: Boolean) = update { setIsDialpadTonesEnabled(isEnabled) }

    fun onIsDialpadVibrateEnabled(isEnabled: Boolean) = update { setIsDialpadVibrateEnabled(isEnabled) }

    fun onClearRecentsClick() {
        viewModelScope.launch {
            runCatching { recentRepository.deleteAllRecents() }
                .onFailure { Log.e(TAG, "Failed clearing recents", it) }
        }
    }

    private fun update(change: suspend PreferenceRepository.() -> Unit) {
        viewModelScope.launch { preferenceRepository.change() }
    }

    private companion object {
        const val TAG = "PreferencesViewModel"
    }
}
