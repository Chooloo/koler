package com.chooloo.www.koler.data

import android.content.Context
import android.net.Uri
import android.telecom.PhoneAccountHandle
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.di.IoScope
import com.chooloo.www.chooloolib.domain.repository.phone.PhoneRepository
import com.chooloo.www.koler.domain.AudioRoute
import com.chooloo.www.koler.domain.CallData
import com.chooloo.www.koler.domain.CallRepository
import com.chooloo.www.koler.domain.CallRepository.CallsState
import com.chooloo.www.koler.domain.CallState
import com.chooloo.www.koler.domain.CallerData
import com.chooloo.www.koler.service.CallService
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@OptIn(ExperimentalCoroutinesApi::class)
@Singleton
class CallRepositoryImpl @Inject constructor(
    private val phoneRepository: PhoneRepository,
    @IoScope private val ioScope: CoroutineScope,
    @ApplicationContext private val context: Context,
) : CallRepository {
    private val serviceFlow = CallService.sInstanceFlow

    private fun isCallsHaveStates(calls: List<CallData>, callStates: List<CallState>) =
        calls.isNotEmpty() && calls.map { it.state }.all { it in callStates }

    override val calls: StateFlow<List<CallData>> =
        serviceFlow.flatMapLatest { it?.calls ?: flowOf(emptyList()) }
            .stateIn(ioScope, SharingStarted.WhileSubscribed(5000), emptyList())

    override val mainCall: StateFlow<CallData?> =
        calls.map { it.firstOrNull() }
            .stateIn(ioScope, SharingStarted.WhileSubscribed(5000), null)

    override val canAddCall: StateFlow<Boolean> =
        serviceFlow.flatMapLatest { it?.canAddCall ?: flowOf(false) }
            .stateIn(ioScope, SharingStarted.WhileSubscribed(5000), false)

    override val isMuteActive: StateFlow<Boolean> =
        serviceFlow.flatMapLatest { service ->
            service?.callAudioState?.map { it?.isMuted ?: false } ?: flowOf(false)
        }.stateIn(ioScope, SharingStarted.WhileSubscribed(5000), false)

    override val audioRoute: StateFlow<AudioRoute?> =
        serviceFlow.flatMapLatest { service ->
            service?.callAudioState?.map { AudioRoute.fromRoute(it?.route) } ?: flowOf(null)
        }.stateIn(ioScope, SharingStarted.WhileSubscribed(5000), null)

    override val isSpeakerActive: StateFlow<Boolean> =
        audioRoute.map { it == AudioRoute.SPEAKER }
            .stateIn(ioScope, SharingStarted.WhileSubscribed(5000), false)

    override val bringToForegroundEvent: Flow<Boolean> =
        serviceFlow.flatMapLatest { it?.bringToForegroundEvent ?: emptyFlow() }

    override val supportedAudioRoutes: StateFlow<List<AudioRoute>> =
        serviceFlow.flatMapLatest { service ->
            service?.callAudioState?.map { state ->
                AudioRoute.entries.filter {
                    state?.supportedRouteMask?.let(it.route::and) == it.route
                }
            } ?: flowOf(emptyList())
        }.stateIn(ioScope, SharingStarted.WhileSubscribed(5000), emptyList())

    override val pendingAccountHandle: StateFlow<PhoneAccountHandle?> =
        serviceFlow.flatMapLatest { it?.pendingAccountHandle ?: flowOf(null) }
            .stateIn(ioScope, SharingStarted.WhileSubscribed(5000), null)

    override suspend fun answerMainCall() {
        mainCall.value?.accept()
    }

    override suspend fun declineMainCall() {
        mainCall.value?.decline()
    }

    override suspend fun getCallCallerData(call: CallData): CallerData {
        val account = phoneRepository.lookupAccount(call.number)
        return CallerData(
            name = if (call.isConference) context.getString(R.string.conference) else account?.displayString
                ?: call.number ?: context.getString(R.string.caller_unknown),
            imageUri = if (account?.photoUri != null) Uri.parse(account.photoUri) else null
        )
    }

    override suspend fun toggleMute(on: Boolean) {
        CallService.sInstance?.setMuted(on)
    }

    override suspend fun toggleSpeaker(on: Boolean) {
        if (on) {
            setAudioRoute(AudioRoute.SPEAKER)
        } else if (audioRoute.value != AudioRoute.BLUETOOTH) {
            setAudioRoute(AudioRoute.WIRED_OR_EARPIECE)
        }
    }

    override suspend fun setAudioRoute(audioRoute: AudioRoute) {
        CallService.sInstance?.setAudioRoute(audioRoute.route)
    }

    override suspend fun filterPrimaryCall(calls: List<CallData>) =
        filterStateCall(calls, CallState.INCOMING) ?: filterStateCall(calls, CallState.DIALING)
        ?: filterStateCall(calls, CallState.REDIALING) ?: filterStateCall(
            calls, CallState.CONNECTING
        ) ?: filterStateCall(calls, CallState.ACTIVE) ?: filterStateCall(
            calls, CallState.DISCONNECTING
        ) ?: filterStateCall(calls, CallState.DISCONNECTED) ?: filterStateCall(
            calls, CallState.HOLDING
        )

    override suspend fun filterStateCall(calls: List<CallData>, state: CallState) =
        calls.firstOrNull { it.state == state }

    override suspend fun getCallsState(
        calls: List<CallData>, pendingAccountHandle: PhoneAccountHandle?
    ) = if (calls.isEmpty()) {
        if (pendingAccountHandle != null) CallsState.OUTGOING else CallsState.NO_CALLS
    } else if (calls.any { it.state in listOf(CallState.INCOMING, CallState.CALL_WAITING) }) {
        if (calls.any { it.state == CallState.ACTIVE }) {
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
            calls, listOf(CallState.ACTIVE, CallState.HOLDING, CallState.DISCONNECTING, CallState.CONFERENCED)
        )
    ) {
        if (calls.size > 1) CallsState.ACTIVE_MULTI else CallsState.ACTIVE
    } else if (isCallsHaveStates(calls, listOf(CallState.DISCONNECTED))) {
        CallsState.DISCONNECTED
    } else {
        CallsState.ACTIVE
    }
}
