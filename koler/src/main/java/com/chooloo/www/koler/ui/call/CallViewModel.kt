package com.chooloo.www.koler.ui.call

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chooloo.www.chooloolib.domain.model.PhoneLookup
import com.chooloo.www.chooloolib.domain.repository.AudioRepository
import com.chooloo.www.chooloolib.domain.repository.PhoneRepository
import com.chooloo.www.chooloolib.domain.repository.PreferenceRepository
import com.chooloo.www.koler.domain.enum.AudioRoute
import com.chooloo.www.koler.domain.enum.CallCapability
import com.chooloo.www.koler.domain.enum.CallState
import com.chooloo.www.koler.domain.enum.CallsState
import com.chooloo.www.koler.domain.model.Call
import com.chooloo.www.koler.domain.model.CallAudio
import com.chooloo.www.koler.domain.model.PhoneAccount
import com.chooloo.www.koler.domain.repository.CallRepository
import com.chooloo.www.koler.domain.usecase.GetCallsState
import com.chooloo.www.koler.domain.usecase.GetPrimaryCall
import com.chooloo.www.koler.domain.usecase.SetSpeaker
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
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
    private val getPrimaryCall: GetPrimaryCall,
    private val getCallsState: GetCallsState,
    private val setSpeaker: SetSpeaker,
) : ViewModel() {
    enum class Sheet { KEYPAD, ADD_CALL, MANAGE, AUDIO_ROUTE }

    data class UiState(
        val call: Call? = null,
        val calls: List<Call> = emptyList(),
        val caller: PhoneLookup? = null,
        val callsState: CallsState = CallsState.NO_CALLS,
        val elapsedSeconds: Long? = null,
        val audio: CallAudio = CallAudio(),
        val actions: List<CallAction> = emptyList(),
        val keypadValue: String = "",
        val sheet: Sheet? = null,
    )

    private data class LocalState(val keypadValue: String = "", val sheet: Sheet? = null)

    private val localState = MutableStateFlow(LocalState())

    private val primaryCall = callRepository.calls.map { getPrimaryCall(it) }

    private val caller = primaryCall.map { it?.number }
        .distinctUntilChanged()
        .mapLatest { number -> number?.let { phoneRepository.lookup(it) } }
        .onStart { emit(null) }

    private val elapsedSeconds = primaryCall.map { call ->
        call?.connectTimeMillis?.takeIf { call.hasStarted }
    }
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
        combine(callRepository.hasPendingOutgoing, callRepository.canAddCall, ::Pair),
        combine(caller, elapsedSeconds, ::Pair),
        localState,
    ) { calls, audio, (hasPendingOutgoing, canAddCall), (caller, elapsed), local ->
        val call = getPrimaryCall(calls)
        val state = getCallsState(calls, hasPendingOutgoing)
        UiState(
            call = call,
            calls = calls,
            caller = caller,
            callsState = state,
            elapsedSeconds = elapsed,
            audio = audio,
            actions = actions(state, call, audio, canAddCall),
            keypadValue = local.keypadValue,
            sheet = local.sheet,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UiState())

    init {
        closeAddCallWhenCallAdded()
    }

    fun onCallActionClick(type: CallActionType) {
        val state = uiState.value
        val callId = state.call?.id
        when (type) {
            CallActionType.SWAP -> callId?.let(callRepository::swap)

            CallActionType.MERGE -> callId?.let(callRepository::merge)

            CallActionType.HOLD -> callId?.let {
                if (state.call?.state == CallState.HOLDING) {
                    callRepository.unhold(it)
                } else {
                    callRepository.hold(it)
                }
            }

            CallActionType.MUTE -> callRepository.setMuted(!state.audio.isMuted)

            CallActionType.KEYPAD -> showSheet(Sheet.KEYPAD)

            CallActionType.ADD_CALL -> showSheet(Sheet.ADD_CALL)

            CallActionType.SPEAKER -> if (AudioRoute.BLUETOOTH in state.audio.supportedRoutes) {
                showSheet(Sheet.AUDIO_ROUTE)
            } else {
                setSpeaker(state.audio.route != AudioRoute.SPEAKER)
            }
        }
    }

    suspend fun awaitCallsEnded() {
        callRepository.calls.first { it.isEmpty() }
    }

    fun onAccept() {
        uiState.value.call?.id?.let(callRepository::answer)
    }

    fun onHangup() {
        uiState.value.call?.id?.let(callRepository::decline)
    }

    fun onManage() = showSheet(Sheet.MANAGE)

    fun onKeypadKeyClick(key: Char) {
        uiState.value.call?.id?.let { callRepository.playDtmf(it, key) }
        localState.update { it.copy(keypadValue = it.keypadValue + key) }
        viewModelScope.launch {
            if (preferenceRepository.settings.first().isDialpadVibrateEnabled) {
                audioRepository.vibrate()
            }
        }
    }

    fun onPhoneAccountSelected(account: PhoneAccount) {
        uiState.value.call?.id?.let { callRepository.selectPhoneAccount(it, account.id) }
    }

    fun onPhoneAccountDismiss() {
        uiState.value.call?.id?.let(callRepository::decline)
    }

    fun onAudioRouteSelected(route: AudioRoute) {
        callRepository.setAudioRoute(route)
        onSheetDismiss()
    }

    fun onHangupCall(call: Call) = callRepository.decline(call.id)

    fun onSeparateCall(call: Call) = callRepository.separate(call.id)

    fun onShowKeypad() = showSheet(Sheet.KEYPAD)

    fun onSheetDismiss() = localState.update { it.copy(sheet = null) }

    private fun closeAddCallWhenCallAdded() {
        viewModelScope.launch {
            var previousCount = callRepository.calls.value.size
            callRepository.calls.collect { calls ->
                if (calls.size > previousCount) {
                    localState.update {
                        if (it.sheet == Sheet.ADD_CALL) {
                            it.copy(sheet = null)
                        } else {
                            it
                        }
                    }
                }
                previousCount = calls.size
            }
        }
    }

    private fun showSheet(sheet: Sheet) = localState.update { it.copy(sheet = sheet) }

    private fun actions(
        state: CallsState,
        call: Call?,
        audio: CallAudio,
        canAddCall: Boolean,
    ): List<CallAction> {
        if (call == null) return emptyList()
        val common = listOf(
            CallAction(CallActionType.KEYPAD, isEnabled = true),
            CallAction(
                CallActionType.MUTE,
                isEnabled = call.can(CallCapability.MUTE),
                isActive = audio.isMuted,
            ),
            CallAction(
                CallActionType.HOLD,
                isEnabled = call.can(CallCapability.HOLD),
                isActive = call.state == CallState.HOLDING,
            ),
            CallAction(CallActionType.ADD_CALL, isEnabled = canAddCall),
        )
        return if (state == CallsState.ACTIVE_MULTI) {
            listOf(
                CallAction(CallActionType.SWAP, isEnabled = call.can(CallCapability.SWAP)),
                CallAction(CallActionType.MERGE, isEnabled = call.can(CallCapability.MERGE)),
            ) + common
        } else {
            common + CallAction(
                CallActionType.SPEAKER,
                isEnabled = true,
                isActive = audio.route == AudioRoute.SPEAKER,
            )
        }
    }
}
