package com.chooloo.www.chooloolib.ui.preferences

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chooloo.www.chooloolib.domain.enum.Page
import com.chooloo.www.chooloolib.domain.enum.ThemeMode
import com.chooloo.www.chooloolib.domain.model.Settings
import com.chooloo.www.chooloolib.domain.repository.PreferenceRepository
import com.chooloo.www.chooloolib.domain.repository.RecentRepository
import com.chooloo.www.chooloolib.ui.common.UserMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class PreferencesViewModel @Inject constructor(
    private val recentRepository: RecentRepository,
    private val preferenceRepository: PreferenceRepository,
) : ViewModel() {
    val settings: StateFlow<Settings?> =
        preferenceRepository.settings.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            null,
        )

    private val _message = MutableStateFlow<UserMessage?>(null)
    val message: StateFlow<UserMessage?> = _message.asStateFlow()

    fun onDefaultPage(page: Page) = update { setDefaultPage(page) }

    fun onThemeMode(themeMode: ThemeMode) = update { setThemeMode(themeMode) }

    fun onIsGroupRecentsEnabled(isEnabled: Boolean) = update { setIsGroupRecentsEnabled(isEnabled) }

    fun onIsDialpadTonesEnabled(isEnabled: Boolean) = update { setIsDialpadTonesEnabled(isEnabled) }

    fun onIsDialpadVibrateEnabled(isEnabled: Boolean) = update {
        setIsDialpadVibrateEnabled(isEnabled)
    }

    fun onClearRecentsClick() {
        viewModelScope.launch {
            runCatching { recentRepository.deleteAllRecents() }
                .onFailure {
                    Log.e(TAG, "Failed clearing recents", it)
                    _message.value = UserMessage.CLEAR_RECENTS_FAILED
                }
        }
    }

    fun onMessageShown() {
        _message.value = null
    }

    private fun update(change: suspend PreferenceRepository.() -> Unit) {
        viewModelScope.launch { preferenceRepository.change() }
    }

    private companion object {
        const val TAG = "PreferencesViewModel"
    }
}
