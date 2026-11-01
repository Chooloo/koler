package com.chooloo.www.chooloolib.ui.viewmodel.call

import android.telecom.Call
import android.telecom.Connection
import android.telecom.PhoneAccountHandle
import androidx.lifecycle.viewModelScope
import com.chooloo.www.chooloolib.domain.model.AudioRoute
import com.chooloo.www.chooloolib.domain.model.CallActionType
import com.chooloo.www.chooloolib.domain.model.CallData
import com.chooloo.www.chooloolib.domain.model.CallState
import com.chooloo.www.chooloolib.domain.model.CallerData
import com.chooloo.www.chooloolib.domain.repository.audio.AudioRepository
import com.chooloo.www.chooloolib.domain.repository.audio.AudioRepository.AudioMode
import com.chooloo.www.chooloolib.domain.repository.call.CallRepository
import com.chooloo.www.chooloolib.domain.repository.call.CallRepository.CallsState
import com.chooloo.www.chooloolib.ui.viewmodel.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CallViewModel @Inject constructor(
    private val callRepository: CallRepository,
    private val audioRepository: AudioRepository
) : BaseViewModel() {
    data class UiState(
        val call: CallData? = null,
        val elapsedSeconds: Long? = null,
        val audioRoute: AudioRoute? = null,
        val isMuteActive: Boolean = false,
        val isManageOpen: Boolean = false,
        val isKeypadOpen: Boolean = false,
        val isDialpadOpen: Boolean = false,
        val isSelectAudioOpen: Boolean = false,
        val isSelectAccountOpen: Boolean = false,
        val calls: List<CallData> = listOf(),
        val callerData: CallerData = CallerData(),
        val callsState: CallsState = CallsState.UNKNOWN,
        val visibleActions: List<CallActionType> = listOf(),
        val enabledActions: List<CallActionType> = listOf(),
        val activatedActions: List<CallActionType> = listOf(),
        val pendingAccountHandle: PhoneAccountHandle? = null,
        val supportedAudioRoutes: List<AudioRoute> = listOf()
    )

    private val _uiState = MutableStateFlow(UiState())

    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            while (true) {
                val currentCall = _uiState.value.call
                val elapsed = if (currentCall != null && currentCall.hasStarted && currentCall.connectMillis > 0) {
                    currentCall.durationMillis / 1000
                } else {
                    null
                }
                _uiState.update { it.copy(elapsedSeconds = elapsed) }
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
                _uiState.update {
                    val next = it.copy(isMuteActive = isMuted)
                    next.copy(activatedActions = computeActivatedActions(next))
                }
            }
        }
        viewModelScope.launch {
            callRepository.audioRoute.collect { route ->
                _uiState.update {
                    val next = it.copy(audioRoute = route)
                    next.copy(activatedActions = computeActivatedActions(next))
                }
            }
        }
        viewModelScope.launch {
            callRepository.supportedAudioRoutes.collect { routes ->
                _uiState.update { it.copy(supportedAudioRoutes = routes) }
            }
        }
    }

    private fun computeVisibleActions(callsState: CallsState): List<CallActionType> =
        if (callsState == CallsState.ACTIVE_MULTI) {
            listOf(
                CallActionType.SWAP,
                CallActionType.MERGE,
                CallActionType.KEYPAD,
                CallActionType.MUTE_ON,
                CallActionType.HOLD_ON,
                CallActionType.ADD_CALL,
            )
        } else {
            listOf(
                CallActionType.KEYPAD,
                CallActionType.MUTE_ON,
                CallActionType.HOLD_ON,
                CallActionType.ADD_CALL,
                CallActionType.SPEAKER_ON
            )
        }

    private fun computeEnabledActions(call: CallData?): List<CallActionType> {
        if (call == null) return emptyList()
        return buildList {
            add(CallActionType.KEYPAD)
            add(CallActionType.ADD_CALL)
            add(CallActionType.SPEAKER_ON)
            add(CallActionType.SPEAKER_OFF)
            if (call.canMute) {
                add(CallActionType.MUTE_ON)
                add(CallActionType.MUTE_OFF)
            }
            if (call.canHold) {
                add(CallActionType.HOLD_ON)
                add(CallActionType.HOLD_OFF)
            }
            if (call.canSwap) {
                add(CallActionType.SWAP)
            }
            if (call.conferenceableCalls.isNotEmpty() || call.isCapable(Connection.CAPABILITY_MERGE_CONFERENCE)) {
                add(CallActionType.MERGE)
            }
        }
    }

    private fun computeActivatedActions(state: UiState): List<CallActionType> = buildList {
        if (state.isMuteActive) {
            add(CallActionType.MUTE_ON)
            add(CallActionType.MUTE_OFF)
        }
        if (state.call?.isHolding == true) {
            add(CallActionType.HOLD_ON)
            add(CallActionType.HOLD_OFF)
        }
        if (state.audioRoute == AudioRoute.SPEAKER) {
            add(CallActionType.SPEAKER_ON)
            add(CallActionType.SPEAKER_OFF)
        }
    }

    fun onDismissKeypad() {
        _uiState.update { it.copy(isKeypadOpen = false) }
    }

    fun onDismissManage() {
        _uiState.update { it.copy(isManageOpen = false) }
    }

    fun onDismissDialpad() {
        _uiState.update { it.copy(isDialpadOpen = false) }
    }

    fun onDismissSelectAudioRouter() {
        _uiState.update { it.copy(isSelectAudioOpen = false) }
    }

    fun onDismissSelectPhoneAccount() {
        viewModelScope.launch {
            callRepository.filterStateCall(_uiState.value.calls, CallState.SELECT_PHONE_ACCOUNT)
                ?.decline()
        }
    }

    fun onKeypadClick(key: Char) {
        _uiState.value.call?.playKey(key)
    }

    fun onCallActionClick(action: CallActionType) {
        val uiState = _uiState.value
        val call = uiState.call
        when (action) {
            CallActionType.DENY -> call?.decline()
            CallActionType.HOLD_ON -> try { call?.hold() } catch (_: Exception) {}
            CallActionType.ACCEPT -> call?.accept()
            CallActionType.HOLD_OFF -> call?.unhold()
            CallActionType.SWAP -> try { call?.swapConference() } catch (_: Exception) {}
            CallActionType.MERGE -> try { call?.mergeConference() } catch (_: Exception) {}
            CallActionType.MUTE_ON -> viewModelScope.launch { callRepository.toggleMute(true) }
            CallActionType.MUTE_OFF -> viewModelScope.launch { callRepository.toggleMute(false) }
            CallActionType.MANAGE -> _uiState.update { it.copy(isManageOpen = true) }
            CallActionType.KEYPAD -> _uiState.update { it.copy(isKeypadOpen = true) }
            CallActionType.ADD_CALL -> _uiState.update { it.copy(isDialpadOpen = true) }
            CallActionType.SPEAKER_ON -> {
                if (AudioRoute.BLUETOOTH in uiState.supportedAudioRoutes) {
                    _uiState.update { it.copy(isSelectAudioOpen = true) }
                } else {
                    viewModelScope.launch { callRepository.toggleSpeaker(true) }
                }
            }
            CallActionType.SPEAKER_OFF -> {
                if (AudioRoute.BLUETOOTH in uiState.supportedAudioRoutes) {
                    _uiState.update { it.copy(isSelectAudioOpen = true) }
                } else {
                    viewModelScope.launch { callRepository.toggleSpeaker(false) }
                }
            }
        }
    }

    fun onSelectAudioRoute(audioRoute: AudioRoute) {
        viewModelScope.launch {
            callRepository.setAudioRoute(audioRoute)
        }
    }

    fun onSelectPhoneAccount(phoneAccount: PhoneAccountHandle) {
        _uiState.value.call?.selectAccount(phoneAccount)
    }

    private fun onCallsChanged(calls: List<CallData>) {
        if (calls.isEmpty()) {
            viewModelScope.launch {
                audioRepository.setAudioMode(AudioMode.NORMAL)
            }
        }

        viewModelScope.launch {
            val primaryCall = callRepository.filterPrimaryCall(calls)
            val callsState = callRepository.getCallsState(calls, _uiState.value.pendingAccountHandle)
            val callerData = primaryCall?.let { callRepository.getCallCallerData(it) } ?: CallerData()

            _uiState.update {
                val next = it.copy(
                    calls = calls,
                    call = primaryCall,
                    callerData = callerData,
                    callsState = callsState,
                    pendingAccountHandle = null,
                    visibleActions = computeVisibleActions(callsState),
                    enabledActions = computeEnabledActions(primaryCall)
                )
                next.copy(activatedActions = computeActivatedActions(next))
            }
        }
    }
}
