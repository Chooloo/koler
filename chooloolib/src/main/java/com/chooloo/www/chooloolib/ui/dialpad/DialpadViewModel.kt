package com.chooloo.www.chooloolib.ui.dialpad

import android.util.Log
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.domain.repository.AudioRepository
import com.chooloo.www.chooloolib.domain.repository.PreferenceRepository
import com.chooloo.www.chooloolib.domain.repository.RecentRepository
import com.chooloo.www.chooloolib.domain.repository.TelecomRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.updateAndGet
import kotlinx.coroutines.launch

@HiltViewModel
class DialpadViewModel @Inject constructor(
    private val audioRepository: AudioRepository,
    private val recentRepository: RecentRepository,
    private val telecomRepository: TelecomRepository,
    private val preferenceRepository: PreferenceRepository,
) : ViewModel() {
    data class UiState(val value: String = "", @StringRes val message: Int? = null)

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    fun setNumber(number: String) {
        _uiState.update { it.copy(value = number.filter(::isDialable)) }
    }

    fun onCallClick() {
        val value = _uiState.value.value
        launchCatching {
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
            val settings = preferenceRepository.settings.first()
            if (settings.isDialpadTonesEnabled) audioRepository.playDtmfTone(key)
            if (settings.isDialpadVibrateEnabled) audioRepository.vibrate()
        }
        val value = _uiState.updateAndGet { it.copy(value = it.value + key) }.value
        viewModelScope.launch { telecomRepository.handleSecretCode(value) }
    }

    fun onKeyLongClick(key: Char) {
        when (key) {
            '0' -> _uiState.update { it.copy(value = it.value + '+') }
            '1' -> launchCatching { telecomRepository.callVoicemail() }
        }
    }

    fun onMessageShown() {
        _uiState.update { it.copy(message = null) }
    }

    private fun launchCatching(action: suspend () -> Unit) {
        viewModelScope.launch {
            runCatching { action() }.onFailure { error ->
                Log.e(TAG, "Call failed", error)
                _uiState.update { it.copy(message = R.string.error_call_failed) }
            }
        }
    }

    private fun isDialable(char: Char) = char.isDigit() || char in "+#*"

    private companion object {
        const val TAG = "DialpadViewModel"
    }
}
