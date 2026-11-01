package com.chooloo.www.koler.ui.call

import androidx.lifecycle.viewModelScope
import com.chooloo.www.chooloolib.domain.repository.audio.AudioRepository
import com.chooloo.www.chooloolib.domain.repository.preference.PreferenceRepository
import com.chooloo.www.chooloolib.ui.common.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class KeypadViewModel @Inject constructor(
    private val audioRepository: AudioRepository,
    private val preferenceRepository: PreferenceRepository,
) : BaseViewModel() {
    data class UiState(
        val value: String = ""
    )

    private val _uiState = MutableStateFlow(UiState())

    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    fun onValueClick(value: String) {
        viewModelScope.launch {
            if (preferenceRepository.isDialpadTonesEnabled.value) {
                audioRepository.playToneByChar(value[0])
            }
            if (preferenceRepository.isDialpadVibrateEnabled.value) {
                audioRepository.vibrate(AudioRepository.SHORT_VIBRATE_LENGTH)
            }
        }
        _uiState.update { it.copy(value = it.value + value) }
    }
}
