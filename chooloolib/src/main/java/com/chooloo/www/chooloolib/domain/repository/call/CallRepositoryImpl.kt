package com.chooloo.www.chooloolib.domain.repository.call

import android.content.Context
import android.net.Uri
import android.os.Build
import android.telecom.Call
import android.telecom.Call.STATE_RINGING
import android.telecom.CallAudioState
import android.telecom.Connection
import android.telecom.PhoneAccountHandle
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.di.module.IoScope
import com.chooloo.www.chooloolib.domain.model.AudioRoute
import com.chooloo.www.chooloolib.domain.model.CallData
import com.chooloo.www.chooloolib.domain.model.CallState
import com.chooloo.www.chooloolib.domain.model.CallerData
import com.chooloo.www.chooloolib.domain.model.ICallData
import com.chooloo.www.chooloolib.domain.model.ICallData.CallException
import com.chooloo.www.chooloolib.domain.repository.base.BaseRepositoryImpl
import com.chooloo.www.chooloolib.domain.repository.call.CallRepository.CallsState
import com.chooloo.www.chooloolib.domain.repository.phone.PhoneRepository
import com.chooloo.www.chooloolib.domain.service.CallService
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.annotation.Nullable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CallRepositoryImpl @Inject constructor(
    private val phoneRepository: PhoneRepository,
    @IoScope private val ioScope: CoroutineScope,
    @Nullable private val callService: CallService?,
    @ApplicationContext private val context: Context,
) : BaseRepositoryImpl(), CallRepository {
    private fun filterMainCall(calls: List<CallData>) =
        calls.firstOrNull()

    private fun isCallsHaveStates(calls: List<ICallData>, callStates: List<CallState>) =
        calls.map { it.state }.all { it in callStates }

    private fun isCallCapable(call: Call, capability: Int) =
        (call.details.callCapabilities and capability) != 0

    override val mainCall: StateFlow<CallData?> = flow {
        calls.collect { emit(filterMainCall(it)) }
    }.stateIn(
        ioScope, SharingStarted.WhileSubscribed(), callService?.calls?.value?.let(::filterMainCall)
    )

    override val canAddCall: StateFlow<Boolean> =
        callService?.canAddCall ?: flow<Boolean> { }.stateIn(
            ioScope, SharingStarted.WhileSubscribed(), false
        )

    override val isMuteActive: StateFlow<Boolean> =
        (callService?.callAudioState ?: flow<CallAudioState> { }).map {
            it?.isMuted ?: false
        }.stateIn(
            ioScope,
            SharingStarted.WhileSubscribed(),
            callService?.callAudioState?.value?.isMuted ?: false
        )

    override val calls: StateFlow<List<CallData>> =
        callService?.calls ?: flow<List<CallData>> { }.stateIn(
            ioScope, SharingStarted.WhileSubscribed(), emptyList()
        )

    override val audioRoute: StateFlow<AudioRoute?> =
        (callService?.callAudioState ?: flow {}).map { AudioRoute.fromRoute(it?.route) }.stateIn(
            ioScope,
            SharingStarted.WhileSubscribed(),
            AudioRoute.fromRoute(callService?.callAudioState?.value?.route)
        )

    override val isSpeakerActive: StateFlow<Boolean> =
        (callService?.callAudioState ?: flow {}).map { it?.route == AudioRoute.SPEAKER.route }
            .stateIn(
                ioScope,
                SharingStarted.WhileSubscribed(),
                callService?.callAudioState?.value?.route == AudioRoute.SPEAKER.route
            )

    override val bringToForegroundEvent: Flow<Boolean> = flow {
        callService?.bringToForegroundEvent?.collect {
            this.emit(it)
        }
    }

    override val supportedAudioRoutes: StateFlow<List<AudioRoute>> =
        (callService?.callAudioState ?: flow {}).map { state ->
            AudioRoute.entries.filter {
                state?.supportedRouteMask?.let(it.route::and) == it.route
            }
        }.stateIn(ioScope, SharingStarted.WhileSubscribed(), AudioRoute.entries.filter {
            callService?.callAudioState?.value?.supportedRouteMask?.let(it.route::and) == it.route
        })

    override val pendingAccountHandle: StateFlow<PhoneAccountHandle?> =
        callService?.pendingAccountHandle ?: flow<PhoneAccountHandle> {}.stateIn(
            ioScope, SharingStarted.WhileSubscribed(), null
        )


    override suspend fun answerMainCall() {
        mainCall.value?.accept()
    }

    override suspend fun declineMainCall() {
        mainCall.value?.decline()
    }

    /**
     * @throws CallException.CantHoldCallException
     */
    override suspend fun hold(call: Call) {
        if (!isCallCapable(call, Call.Details.CAPABILITY_HOLD)) {
            throw CallException.CantHoldCallException()
        }
        call.hold()
    }

    /**
     * Merge the call
     * If there are conferenceable calls,
     * make the first conferenceable call (probably the first call in this session)
     * conference the new call (which will add the new call to existing conference if exist)
     * Otherwise, if call is capable of "merge conference", do it
     * Otherwise, throw an exception saying call cant be merge
     *
     * @throws CallException.CantMergeCallException
     */
    override suspend fun merge(call: Call) {
        val conferenceableCalls = call.conferenceableCalls
        when {
            conferenceableCalls.isNotEmpty() -> conferenceableCalls[0].conference(call)
            isCallCapable(call, Connection.CAPABILITY_MERGE_CONFERENCE) -> call.mergeConference()
            else -> throw CallException.CantMergeCallException()
        }
    }

    override suspend fun unhold(call: Call) {
        call.unhold()
    }

    override suspend fun answer(call: Call) {
        call.answer(call.details.videoState)
    }

    /**
     * Usually not capable
     * Swaps between foreground call and backend call
     *
     * @throws CallException.CantSwapCallException
     */
    override suspend fun swapConference(call: Call) {
        if (!isCallCapable(call, Connection.CAPABILITY_SWAP_CONFERENCE)) {
            throw CallException.CantSwapCallException()
        }
        call.swapConference()
    }

    /**
     * @throws CallException.CantLeaveConferenceException
     */
    override suspend fun leaveConference(call: Call) {
        if (!isCallCapable(call, Connection.CAPABILITY_SEPARATE_FROM_CONFERENCE)) {
            throw CallException.CantLeaveConferenceException()
        }
        call.splitFromConference()
    }

    override suspend fun playCallKey(call: Call, c: Char) {
        call.playDtmfTone(c)
        call.stopDtmfTone()
    }

    override suspend fun hangup(call: Call, message: String) {
        @Suppress("DEPRECATION") if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && call.details.state == STATE_RINGING) || call.state == STATE_RINGING) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                call.reject(Call.REJECT_REASON_DECLINED)
            } else {
                call.reject(false, message)
            }
        } else {
            call.disconnect()
        }
    }

    override suspend fun joinConference(call: Call, otherCall: Call) {
        call.conference(otherCall)
    }

    override suspend fun selectPhoneAccount(call: Call, accountHandle: PhoneAccountHandle) {
        call.phoneAccountSelected(accountHandle, false)
    }

    override suspend fun getCallCallerData(call: ICallData): CallerData {
        val account = phoneRepository.lookupAccount(call.number)
        return CallerData(
            name = if (call.isConference) context.getString(R.string.conference) else account?.displayString
                ?: call.number,
            imageUri = if (account?.photoUri != null) Uri.parse(account.photoUri) else null
        )
    }

    override suspend fun toggleMute(on: Boolean) {
        callService?.setMuted(on)
    }

    override suspend fun toggleSpeaker(on: Boolean) {
        if (on) {
            setAudioRoute(AudioRoute.SPEAKER)
        } else if (AudioRoute.fromRoute(callService?.callAudioState?.value?.route) != AudioRoute.BLUETOOTH) {
            setAudioRoute(AudioRoute.WIRED_OR_EARPIECE)
        }
    }

    override suspend fun setAudioRoute(audioRoute: AudioRoute) {
        callService?.setAudioRoute(audioRoute.route)
    }

    override suspend fun filterPrimaryCall(calls: List<ICallData>) =
        filterStateCall(calls, CallState.INCOMING) ?: filterStateCall(calls, CallState.DIALING)
        ?: filterStateCall(calls, CallState.REDIALING) ?: filterStateCall(
            calls, CallState.CONNECTING
        ) ?: filterStateCall(calls, CallState.ACTIVE) ?: filterStateCall(
            calls, CallState.DISCONNECTING
        ) ?: filterStateCall(calls, CallState.DISCONNECTED) ?: filterStateCall(
            calls, CallState.HOLDING
        ) ?: filterStateCall(calls, CallState.DISCONNECTED)

    override suspend fun filterStateCall(calls: List<ICallData>, state: CallState) =
        calls.firstOrNull { it.state == state }

    override suspend fun getCallsState(
        calls: List<ICallData>, pendingAccountHandle: PhoneAccountHandle?
    ) = if (isCallsHaveStates(calls, listOf(CallState.INCOMING, CallState.CALL_WAITING))) {
        if (isCallsHaveStates(calls, listOf(CallState.ACTIVE))) {
            CallsState.EXTRA_INCOMING
        } else {
            CallsState.INCOMING
        }
    } else if (isCallsHaveStates(calls, listOf(CallState.SELECT_PHONE_ACCOUNT))) {
        CallsState.WAITING_FOR_ACCOUNT
    } else if (isCallsHaveStates(calls, listOf(CallState.CONNECTING))) {
        CallsState.PENDING_OUTGOING
    } else if (isCallsHaveStates(calls, listOf(CallState.DIALING, CallState.REDIALING))) {
        CallsState.OUTGOING
    } else if (isCallsHaveStates(
            calls, listOf(CallState.ACTIVE, CallState.HOLDING, CallState.DISCONNECTING)
        )
    ) {
        CallsState.ACTIVE
    } else if (isCallsHaveStates(calls, listOf(CallState.DISCONNECTED))) {
        CallsState.DISCONNECTED
    } else if (pendingAccountHandle != null) {
        CallsState.OUTGOING
    } else {
        CallsState.NO_CALLS
    }
}
