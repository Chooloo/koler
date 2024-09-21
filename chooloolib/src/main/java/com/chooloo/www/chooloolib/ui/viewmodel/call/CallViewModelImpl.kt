package com.chooloo.www.chooloolib.ui.viewmodel.call

import android.telecom.PhoneAccountHandle
import androidx.lifecycle.viewModelScope
import com.chooloo.www.chooloolib.domain.model.AudioRoute
import com.chooloo.www.chooloolib.domain.model.CallActionType
import com.chooloo.www.chooloolib.domain.model.CallState
import com.chooloo.www.chooloolib.domain.model.CallUiMode
import com.chooloo.www.chooloolib.domain.model.ICallData
import com.chooloo.www.chooloolib.domain.repository.audio.AudioRepository
import com.chooloo.www.chooloolib.domain.repository.audio.AudioRepository.AudioMode
import com.chooloo.www.chooloolib.domain.repository.call.CallRepository
import com.chooloo.www.chooloolib.ui.viewmodel.base.BaseViewModelImpl
import com.chooloo.www.chooloolib.ui.viewmodel.call.CallViewModel.Companion.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CallViewModelImpl @Inject constructor(
    private val callRepository: CallRepository,
    private val audioRepository: AudioRepository
) : BaseViewModelImpl(), CallViewModel {
    val visibleActions = when (CallUiMode.MULTI) {
        CallUiMode.MULTI -> listOf(
            CallActionType.SWAP,
            CallActionType.MERGE,
            CallActionType.KEYPAD,
            CallActionType.MUTE_ON,
            CallActionType.HOLD_ON,
            CallActionType.ADD_CALL,
        )

        else -> listOf(
            CallActionType.KEYPAD,
            CallActionType.MUTE_ON,
            CallActionType.HOLD_ON,
            CallActionType.ADD_CALL,
            CallActionType.SPEAKER_ON
        )
    }

    private val _uiState = MutableStateFlow(UiState())

    override val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            while (true) {
                _uiState.update {
                    onCallsChanged(it.calls)
                    it
                }
                delay(1000)
            }
        }

        viewModelScope.launch {
            callRepository.calls.collect(::onCallsChanged)
        }
        viewModelScope.launch {
            callRepository.pendingAccountHandle.collect { handle ->
                _uiState.update { it.copy(pendingAccountHandle = handle) }
            }
        }
        viewModelScope.launch {
            callRepository.isMuteActive.collect { isMuted ->
                _uiState.update { it.copy(isMuteActive = isMuted ?: false) }
            }
        }
        viewModelScope.launch {
            callRepository.audioRoute.collect { route ->
                _uiState.update { it.copy(audioRoute = route) }
            }
        }
    }

    override fun onDismissKeypad() {
        _uiState.update { it.copy(isKeypadOpen = false) }
    }

    override fun onDismissManage() {
        _uiState.update { it.copy(isManageOpen = false) }
    }

    override fun onDismissDialpad() {
        _uiState.update { it.copy(isDialpadOpen = false) }
    }

    override fun onDismissSelectAudioRouter() {
        _uiState.update { it.copy(isSelectAccountOpen = false) }
    }

    override fun onDismissSelectPhoneAccount() {
        viewModelScope.launch {
            callRepository.filterStateCall(_uiState.value.calls, CallState.SELECT_PHONE_ACCOUNT)
                ?.decline()
        }
    }

    override fun onKeypadClick(key: Char) {
        _uiState.value.call?.playKey(key)
    }

    override fun onCallActionClick(action: CallActionType) {
        _uiState.update { uiState ->
            val call = uiState.call
            var newState = uiState
            when (action) {
                CallActionType.DENY -> call?.decline()
                CallActionType.HOLD_ON -> call?.hold()
                CallActionType.ACCEPT -> call?.accept()
                CallActionType.HOLD_OFF -> call?.unhold()
                CallActionType.SWAP -> call?.swapConference()
                CallActionType.MERGE -> call?.mergeConference()
                CallActionType.MUTE_ON -> viewModelScope.launch { callRepository.toggleMute(true) }
                CallActionType.MUTE_OFF -> viewModelScope.launch { callRepository.toggleMute(false) }
                CallActionType.MANAGE -> newState = newState.copy(isManageOpen = true)
                CallActionType.KEYPAD -> newState = newState.copy(isKeypadOpen = true)
                CallActionType.ADD_CALL -> newState = newState.copy(isDialpadOpen = true)
                CallActionType.SPEAKER_ON -> {
                    _uiState.update {
                        if (AudioRoute.BLUETOOTH in it.supportedAudioRoutes) {
                            it.copy(isSelectAudioOpen = true)
                        } else {
                            viewModelScope.launch { callRepository.toggleSpeaker(true) }
                            it
                        }
                    }
                }

                CallActionType.SPEAKER_OFF -> {
                    _uiState.update {
                        if (AudioRoute.BLUETOOTH in it.supportedAudioRoutes) {
                            it.copy(isSelectAudioOpen = true)
                        } else {
                            viewModelScope.launch { callRepository.toggleSpeaker(false) }
                            it
                        }
                    }
                }
            }
            newState
        }
    }

    override fun onSelectAudioRoute(audioRoute: AudioRoute) {
        viewModelScope.launch {
            callRepository.setAudioRoute(audioRoute)
        }
    }

    override fun onSelectPhoneAccount(phoneAccount: PhoneAccountHandle) {
        _uiState.value.call?.selectAccount(phoneAccount)
    }

    private fun onCallsChanged(calls: List<ICallData>) {
        if (calls.isEmpty()) {
            viewModelScope.launch {
                audioRepository.setAudioMode(AudioMode.NORMAL)
            }
        }

        viewModelScope.launch {
            val primaryCall = callRepository.filterPrimaryCall(calls)

            _uiState.update {
                it.copy(
                    calls = calls,
                    call = primaryCall,
                    pendingAccountHandle = null,
                    callsState = callRepository.getCallsState(calls, it.pendingAccountHandle),
                )
            }

            primaryCall?.let {
                _uiState.update { it.copy(callerData = callRepository.getCallCallerData(primaryCall)) }
            }
        }

    }
}