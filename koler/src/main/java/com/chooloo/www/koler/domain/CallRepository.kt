package com.chooloo.www.koler.domain

import android.telecom.PhoneAccountHandle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface CallRepository {
    val mainCall: StateFlow<CallData?>
    val canAddCall: StateFlow<Boolean>
    val calls: StateFlow<List<CallData>>
    val isMuteActive: StateFlow<Boolean>
    val audioRoute: StateFlow<AudioRoute?>
    val isSpeakerActive: StateFlow<Boolean>
    val supportedAudioRoutes: StateFlow<List<AudioRoute>>
    val pendingAccountHandle: StateFlow<PhoneAccountHandle?>

    val bringToForegroundEvent: Flow<Boolean>

    suspend fun answerMainCall()
    suspend fun declineMainCall()
    suspend fun getCallCallerData(call: CallData): CallerData

    suspend fun toggleMute(on: Boolean)
    suspend fun toggleSpeaker(on: Boolean)
    suspend fun setAudioRoute(audioRoute: AudioRoute)

    suspend fun filterPrimaryCall(calls: List<CallData>): CallData?
    suspend fun filterStateCall(calls: List<CallData>, state: CallState): CallData?
    suspend fun getCallsState(
        calls: List<CallData>,
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
