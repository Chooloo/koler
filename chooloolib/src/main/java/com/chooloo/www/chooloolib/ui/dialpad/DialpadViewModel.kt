package com.chooloo.www.chooloolib.ui.dialpad

import androidx.lifecycle.viewModelScope
import com.chooloo.www.chooloolib.domain.repository.AudioRepository
import com.chooloo.www.chooloolib.domain.repository.PreferenceRepository
import com.chooloo.www.chooloolib.domain.repository.RecentRepository
import com.chooloo.www.chooloolib.domain.repository.TelecomRepository
import com.chooloo.www.chooloolib.ui.common.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.updateAndGet
import kotlinx.coroutines.launch

@HiltViewModel
class DialpadViewModel @Inject constructor(
    private val audioRepository: AudioRepository,
    private val recentRepository: RecentRepository,
    private val telecomRepository: TelecomRepository,
    private val preferenceRepository: PreferenceRepository,
) : BaseViewModel() {
    data class UiState(val value: String = "")

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    fun setNumber(number: String) {
        _uiState.update { it.copy(value = number.filter(::isDialable)) }
    }

    fun onCallClick() {
        val value = _uiState.value.value
        viewModelScope.launch {
            if (value.isNotEmpty()) {
                telecomRepository.callNumber(value)
            } else {
                recentRepository.lastOutgoingNumber()?.let(::setNumber)
            }
        }
    }

    fun onDeleteClick() {
        _uiState.update { it.copy(value = it.value.dropLast(1)) }
    }

    fun onDeleteLongClick() {
        _uiState.update { it.copy(value = "") }
    }

    fun onKeyClick(key: Char) {
        viewModelScope.launch {
            if (preferenceRepository.isDialpadTonesEnabled.value) audioRepository.playDtmfTone(key)
            if (preferenceRepository.isDialpadVibrateEnabled.value) audioRepository.vibrate()
        }
        val value = _uiState.updateAndGet { it.copy(value = it.value + key) }.value
        viewModelScope.launch { telecomRepository.handleSecretCode(value) }
    }

    fun onKeyLongClick(key: Char) {
        when (key) {
            '0' -> _uiState.update { it.copy(value = it.value + '+') }
            '1' -> viewModelScope.launch { telecomRepository.callVoicemail() }
        }
    }

    private fun isDialable(char: Char) = char.isDigit() || char in "+#*"
}
