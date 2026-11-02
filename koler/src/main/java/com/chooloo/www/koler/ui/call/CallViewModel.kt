package com.chooloo.www.koler.ui.call

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chooloo.www.chooloolib.domain.model.PhoneLookup
import com.chooloo.www.chooloolib.domain.repository.AudioRepository
import com.chooloo.www.chooloolib.domain.repository.PhoneRepository
import com.chooloo.www.chooloolib.domain.repository.PreferenceRepository
import com.chooloo.www.koler.domain.AudioRoute
import com.chooloo.www.koler.domain.Call
import com.chooloo.www.koler.domain.CallAudio
import com.chooloo.www.koler.domain.CallRepository
import com.chooloo.www.koler.domain.CallState
import com.chooloo.www.koler.domain.CallsState
import com.chooloo.www.koler.domain.callsState
import com.chooloo.www.koler.domain.primaryCall
import com.chooloo.www.koler.domain.setSpeaker
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class CallViewModel @Inject constructor(
    private val callRepository: CallRepository,
    private val phoneRepository: PhoneRepository,
    private val audioRepository: AudioRepository,
    private val preferenceRepository: PreferenceRepository,
) : ViewModel() {
    enum class Sheet { KEYPAD, ADD_CALL, MANAGE, AUDIO_ROUTE }

    data class UiState(
        val call: Call? = null,
        val calls: List<Call> = emptyList(),
        val caller: PhoneLookup? = null,
        val callsState: CallsState = CallsState.NO_CALLS,
        val elapsedSeconds: Long? = null,
        val audio: CallAudio = CallAudio(),
        val visibleActions: List<CallActionType> = emptyList(),
        val enabledActions: List<CallActionType> = emptyList(),
        val activatedActions: List<CallActionType> = emptyList(),
        val keypadValue: String = "",
        val sheet: Sheet? = null,
    )

    private data class LocalState(val keypadValue: String = "", val sheet: Sheet? = null)

    private val localState = MutableStateFlow(LocalState())

    private val primaryCall = callRepository.calls.map(::primaryCall)

    private val caller = primaryCall.map { it?.number }
        .distinctUntilChanged()
        .mapLatest { number -> number?.let { phoneRepository.lookup(it) } }
        .onStart { emit(null) }

    private val elapsedSeconds = primaryCall.map { call -> call?.connectTimeMillis?.takeIf { call.hasStarted } }
        .distinctUntilChanged()
        .flatMapLatest { connectTime ->
            if (connectTime == null) {
                flowOf<Long?>(null)
            } else {
                flow<Long?> {
                    while (true) {
                        emit((System.currentTimeMillis() - connectTime) / 1000)
                        delay(1000)
                    }
                }
            }
        }

    val uiState: StateFlow<UiState> = combine(
        callRepository.calls,
        callRepository.audio,
        callRepository.hasPendingOutgoing,
        combine(caller, elapsedSeconds, ::Pair),
        localState,
    ) { calls, audio, hasPendingOutgoing, (caller, elapsed), local ->
        val call = primaryCall(calls)
        val state = callsState(calls, hasPendingOutgoing)
        UiState(
            call = call,
            calls = calls,
            caller = caller,
            callsState = state,
            elapsedSeconds = elapsed,
            audio = audio,
            visibleActions = visibleActions(state),
            enabledActions = enabledActions(call),
            activatedActions = activatedActions(call, audio),
            keypadValue = local.keypadValue,
            sheet = local.sheet,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UiState())

    fun onCallActionClick(action: CallActionType) {
        val callId = uiState.value.call?.id
        when (action) {
            CallActionType.ACCEPT -> callId?.let(callRepository::answer)
            CallActionType.DENY -> callId?.let(callRepository::decline)
            CallActionType.HOLD_ON -> callId?.let(callRepository::hold)
            CallActionType.HOLD_OFF -> callId?.let(callRepository::unhold)
            CallActionType.SWAP -> callId?.let(callRepository::swap)
            CallActionType.MERGE -> callId?.let(callRepository::merge)
            CallActionType.MUTE_ON -> callRepository.setMuted(true)
            CallActionType.MUTE_OFF -> callRepository.setMuted(false)
            CallActionType.MANAGE -> showSheet(Sheet.MANAGE)
            CallActionType.KEYPAD -> showSheet(Sheet.KEYPAD)
            CallActionType.ADD_CALL -> showSheet(Sheet.ADD_CALL)
            CallActionType.SPEAKER_ON, CallActionType.SPEAKER_OFF -> {
                if (AudioRoute.BLUETOOTH in uiState.value.audio.supportedRoutes) {
                    showSheet(Sheet.AUDIO_ROUTE)
                } else {
                    callRepository.setSpeaker(action == CallActionType.SPEAKER_ON)
                }
            }
        }
    }

    fun onKeypadKeyClick(key: Char) {
        uiState.value.call?.id?.let { callRepository.playDtmf(it, key) }
        localState.update { it.copy(keypadValue = it.keypadValue + key) }
        viewModelScope.launch {
            if (preferenceRepository.isDialpadVibrateEnabled.value) audioRepository.vibrate()
        }
    }

    fun onAudioRouteSelected(route: AudioRoute) {
        callRepository.setAudioRoute(route)
        onSheetDismiss()
    }

    fun onHangupCall(call: Call) = callRepository.decline(call.id)

    fun onSeparateCall(call: Call) = callRepository.separate(call.id)

    fun onShowKeypad() = showSheet(Sheet.KEYPAD)

    fun onSheetDismiss() = localState.update { it.copy(sheet = null) }

    private fun showSheet(sheet: Sheet) = localState.update { it.copy(sheet = sheet) }

    private fun visibleActions(state: CallsState) =
        if (state == CallsState.ACTIVE_MULTI) {
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
                CallActionType.SPEAKER_ON,
            )
        }

    private fun enabledActions(call: Call?) = buildList {
        if (call == null) return@buildList
        addAll(listOf(CallActionType.KEYPAD, CallActionType.ADD_CALL, CallActionType.SPEAKER_ON, CallActionType.SPEAKER_OFF))
        if (call.can(Call.Capability.MUTE)) addAll(listOf(CallActionType.MUTE_ON, CallActionType.MUTE_OFF))
        if (call.can(Call.Capability.HOLD)) addAll(listOf(CallActionType.HOLD_ON, CallActionType.HOLD_OFF))
        if (call.can(Call.Capability.SWAP)) add(CallActionType.SWAP)
        if (call.can(Call.Capability.MERGE)) add(CallActionType.MERGE)
    }

    private fun activatedActions(call: Call?, audio: CallAudio) = buildList {
        if (audio.isMuted) addAll(listOf(CallActionType.MUTE_ON, CallActionType.MUTE_OFF))
        if (call?.state == CallState.HOLDING) addAll(listOf(CallActionType.HOLD_ON, CallActionType.HOLD_OFF))
        if (audio.route == AudioRoute.SPEAKER) addAll(listOf(CallActionType.SPEAKER_ON, CallActionType.SPEAKER_OFF))
    }
}
