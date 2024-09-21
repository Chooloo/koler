package com.chooloo.www.chooloolib.domain.repository.call

import android.telecom.Call
import android.telecom.PhoneAccountHandle
import com.chooloo.www.chooloolib.domain.model.AudioRoute
import com.chooloo.www.chooloolib.domain.model.CallData
import com.chooloo.www.chooloolib.domain.model.CallState
import com.chooloo.www.chooloolib.domain.model.CallerData
import com.chooloo.www.chooloolib.domain.model.ICallData
import com.chooloo.www.chooloolib.domain.repository.base.BaseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface CallRepository : BaseRepository {
    val mainCall: StateFlow<CallData?>
    val canAddCall: StateFlow<Boolean>
    val calls: StateFlow<List<CallData>>
    val isMuteActive: StateFlow<Boolean?>
    val audioRoute: StateFlow<AudioRoute?>
    val isSpeakerActive: StateFlow<Boolean?>
    val supportedAudioRoutes: StateFlow<List<AudioRoute>>
    val pendingAccountHandle: StateFlow<PhoneAccountHandle?>

    val bringToForegroundEvent: Flow<Boolean>

    suspend fun answerMainCall()
    suspend fun declineMainCall()

    suspend fun hold(call: Call)
    suspend fun merge(call: Call)
    suspend fun unhold(call: Call)
    suspend fun answer(call: Call)
    suspend fun swapConference(call: Call)
    suspend fun leaveConference(call: Call)
    suspend fun playCallKey(call: Call, c: Char)
    suspend fun hangup(call: Call, message: String = "")
    suspend fun joinConference(call: Call, otherCall: Call)
    suspend fun selectPhoneAccount(call: Call, accountHandle: PhoneAccountHandle)
    suspend fun getCallCallerData(call: ICallData): CallerData

    suspend fun toggleMute(on: Boolean)
    suspend fun toggleSpeaker(on: Boolean)
    suspend fun setAudioRoute(audioRoute: AudioRoute)

    suspend fun filterPrimaryCall(calls: List<ICallData>): ICallData?
    suspend fun filterStateCall(calls: List<ICallData>, state: CallState): ICallData?
    suspend fun getCallsState(
        calls: List<ICallData>,
        pendingAccountHandle: PhoneAccountHandle?
    ): CallsState

    enum class CallsState {
        ACTIVE,
        UNKNOWN,
        NO_CALLS,
        INCOMING,
        OUTGOING,
        ACTIVE_MULTI,
        DISCONNECTED,
        EXTRA_INCOMING,
        PENDING_OUTGOING,
        WAITING_FOR_ACCOUNT
    }
}