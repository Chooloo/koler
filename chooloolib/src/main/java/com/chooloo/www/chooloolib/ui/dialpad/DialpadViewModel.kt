package com.chooloo.www.chooloolib.ui.dialpad

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chooloo.www.chooloolib.domain.repository.AudioRepository
import com.chooloo.www.chooloolib.domain.repository.PreferenceRepository
import com.chooloo.www.chooloolib.domain.repository.RecentRepository
import com.chooloo.www.chooloolib.domain.repository.TelecomRepository
import com.chooloo.www.chooloolib.ui.common.UserMessage
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
    data class UiState(val input: DialpadInput = DialpadInput(), val message: UserMessage? = null)

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    fun setNumber(number: String) {
        onInputChange(DialpadInput(number))
    }

    fun onInputChange(input: DialpadInput) {
        val text = input.text.filter(::isDialable)
        val cursor = input.text.take(input.selectionEnd).count(::isDialable)
        val accepted = if (text == input.text) input else DialpadInput(text, cursor)
        _uiState.update { it.copy(input = accepted) }
    }

    fun onCallClick() {
        val value = _uiState.value.input.text
        launchCatching {
            if (value.isNotEmpty()) {
                telecomRepository.callNumber(value)
            } else {
                recentRepository.lastOutgoingNumber()?.let(::setNumber)
            }
        }
    }

    fun onDeleteClick() {
        _uiState.update { it.copy(input = it.input.deleteBackward()) }
    }

    fun onDeleteLongClick() {
        _uiState.update { it.copy(input = DialpadInput()) }
    }

    fun onKeyClick(key: Char) {
        viewModelScope.launch {
            val settings = preferenceRepository.settings.first()
            if (settings.isDialpadTonesEnabled) audioRepository.playTone(key)
            if (settings.isDialpadVibrateEnabled) audioRepository.vibrate()
        }
        val state = _uiState.updateAndGet { it.copy(input = it.input.replaceSelection("$key")) }
        viewModelScope.launch {
            runCatching { telecomRepository.handleSecretCode(state.input.text) }
                .onFailure { Log.e(TAG, "Secret code failed", it) }
        }
    }

    fun onKeyLongClick(key: Char) {
        when (key) {
            '0' -> _uiState.update { it.copy(input = it.input.replaceSelection("+")) }
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
                _uiState.update { it.copy(message = UserMessage.CALL_FAILED) }
            }
        }
    }

    private companion object {
        const val TAG = "DialpadViewModel"

        fun isDialable(char: Char) = char.isDigit() || char in "+#*"
    }
}
