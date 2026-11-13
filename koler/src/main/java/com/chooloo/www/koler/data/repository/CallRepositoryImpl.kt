package com.chooloo.www.koler.data.repository

import android.os.Build
import android.telecom.Call as TelecomCall
import android.telecom.CallAudioState
import android.telecom.InCallService
import android.util.Log
import com.chooloo.www.koler.data.mapper.CallMapper
import com.chooloo.www.koler.domain.enum.AudioRoute
import com.chooloo.www.koler.domain.model.Call
import com.chooloo.www.koler.domain.model.CallAudio
import com.chooloo.www.koler.domain.repository.CallRepository
import java.util.IdentityHashMap
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@Singleton
class CallRepositoryImpl @Inject constructor(private val callMapper: CallMapper) :
    CallRepository {
    private var service: InCallService? = null
    private var nextCallId = 0
    private val telecomCalls = LinkedHashMap<String, TelecomCall>()
    private val callIds = IdentityHashMap<TelecomCall, String>()
    private val callCallback = object : TelecomCall.Callback() {
        override fun onStateChanged(call: TelecomCall, state: Int) = publishCalls()
        override fun onParentChanged(call: TelecomCall, parent: TelecomCall?) = publishCalls()
        override fun onChildrenChanged(call: TelecomCall, children: List<TelecomCall>) =
            publishCalls()
        override fun onDetailsChanged(call: TelecomCall, details: TelecomCall.Details) =
            publishCalls()
        override fun onConferenceableCallsChanged(call: TelecomCall, calls: List<TelecomCall>) =
            publishCalls()
    }

    private val _calls = MutableStateFlow<List<Call>>(emptyList())
    private val _audio = MutableStateFlow(CallAudio())
    private val _canAddCall = MutableStateFlow(false)
    private val _hasPendingOutgoing = MutableStateFlow(false)

    override val calls: StateFlow<List<Call>> = _calls.asStateFlow()
    override val audio: StateFlow<CallAudio> = _audio.asStateFlow()
    override val canAddCall: StateFlow<Boolean> = _canAddCall.asStateFlow()
    override val hasPendingOutgoing: StateFlow<Boolean> = _hasPendingOutgoing.asStateFlow()

    override fun answer(callId: String) = withCall(callId) { answer(details.videoState) }

    override fun decline(callId: String) = withCall(callId) {
        when {
            callMapper.telecomState(this) != TelecomCall.STATE_RINGING -> disconnect()

            Build.VERSION.SDK_INT >= Build.VERSION_CODES.R -> reject(
                TelecomCall.REJECT_REASON_DECLINED,
            )

            else -> reject(false, null)
        }
    }

    override fun hold(callId: String) = withCall(callId) { hold() }

    override fun unhold(callId: String) = withCall(callId) { unhold() }

    override fun swap(callId: String) = withCall(callId) { swapConference() }

    override fun merge(callId: String) = withCall(callId) {
        conferenceableCalls.firstOrNull()?.conference(this) ?: mergeConference()
    }

    override fun separate(callId: String) = withCall(callId) { splitFromConference() }

    override fun playDtmf(callId: String, key: Char) = withCall(callId) {
        playDtmfTone(key)
        stopDtmfTone()
    }

    override fun selectPhoneAccount(callId: String, accountId: String) = withCall(callId) {
        val handle = callMapper.availablePhoneAccounts(this)
            .firstOrNull { callMapper.accountId(it) == accountId }
        if (handle == null) {
            Log.e(TAG, "Phone account $accountId is not available for call $callId")
            return@withCall
        }
        phoneAccountSelected(handle, false)
    }

    override fun setMuted(isMuted: Boolean) = withService { setMuted(isMuted) }

    override fun setAudioRoute(route: AudioRoute) = withService {
        setAudioRoute(callMapper.toTelecomRoute(route))
    }

    fun onServiceCreated(service: InCallService) {
        this.service = service
    }

    fun onServiceDestroyed(service: InCallService) {
        if (this.service !== service) return
        this.service = null
        callIds.keys.forEach { it.unregisterCallback(callCallback) }
        telecomCalls.clear()
        callIds.clear()
        _calls.value = emptyList()
        _audio.value = CallAudio()
        _canAddCall.value = false
        _hasPendingOutgoing.value = false
    }

    fun onCallAdded(call: TelecomCall): Call {
        val id = "call-${nextCallId++}"
        callIds[call] = id
        telecomCalls[id] = call
        call.registerCallback(callCallback)
        _hasPendingOutgoing.value = false
        publishCalls()
        return _calls.value.first { it.id == id }
    }

    fun onCallRemoved(call: TelecomCall) {
        call.unregisterCallback(callCallback)
        callIds.remove(call)?.let(telecomCalls::remove)
        publishCalls()
    }

    fun onPendingOutgoing() {
        _hasPendingOutgoing.value = true
    }

    fun onCanAddCallChanged(canAddCall: Boolean) {
        _canAddCall.value = canAddCall
    }

    fun onAudioStateChanged(audioState: CallAudioState) {
        _audio.value = callMapper.toCallAudio(audioState)
    }

    private fun publishCalls() {
        _calls.value = telecomCalls.map { (id, call) -> callMapper.toCall(call, id) }
    }

    private inline fun withService(action: InCallService.() -> Unit) {
        val service = service
        if (service == null) {
            Log.e(TAG, "Ignoring action, no in-call service is bound")
            return
        }
        service.action()
    }

    private inline fun withCall(callId: String, action: TelecomCall.() -> Unit) {
        val call = telecomCalls[callId]
        if (call == null) {
            Log.w(TAG, "Ignoring action on removed call $callId")
            return
        }
        call.action()
    }

    private companion object {
        const val TAG = "CallRepository"
    }
}
