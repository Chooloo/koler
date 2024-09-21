package com.chooloo.www.chooloolib.ui.viewmodel.keypad

import androidx.lifecycle.viewModelScope
import com.chooloo.www.chooloolib.domain.repository.audio.AudioRepository
import com.chooloo.www.chooloolib.domain.repository.preference.PreferenceRepository
import com.chooloo.www.chooloolib.ui.viewmodel.base.BaseViewModelImpl
import com.chooloo.www.chooloolib.ui.viewmodel.keypad.KeypadViewModel.Companion.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

open class KeypadViewModelImpl(
    private val audioRepository: AudioRepository,
    private val preferenceRepository: PreferenceRepository,
) : BaseViewModelImpl(), KeypadViewModel {
    private val _uiState = MutableStateFlow(UiState(value = ""))

    override val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    protected fun setValue(change: (prev: String) -> String) {
        _uiState.update { it.copy(value = change(it.value)) }
    }

    override fun onValueClick(value: String) {
        viewModelScope.launch {
            if (preferenceRepository.isDialpadTonesEnabled.value) audioRepository.playToneByChar(
                value[0]
            )
            if (preferenceRepository.isDialpadVibrateEnabled.value) audioRepository.vibrate(
                AudioRepository.SHORT_VIBRATE_LENGTH
            )
        }
        setValue { it + value }
    }
}

