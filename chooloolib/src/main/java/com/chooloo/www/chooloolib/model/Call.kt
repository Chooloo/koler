package com.chooloo.www.chooloolib.model

import android.net.Uri
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.Q
import android.os.Bundle
import android.telecom.Call.*
import android.telecom.Call.Details.CAPABILITY_HOLD
import android.telecom.Call.Details.PROPERTY_ENTERPRISE_CALL
import android.telecom.Connection
import android.telecom.DisconnectCause
import android.telecom.PhoneAccountHandle
import android.telecom.PhoneAccountSuggestion
import androidx.annotation.RequiresApi
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.model.Call.State.*
import com.chooloo.www.chooloolib.util.baseobservable.BaseObservable
import java.util.stream.Collectors
import java.util.stream.Stream

class CantHoldCallException : Exception()
class CantSwapCallException : Exception()
class CantMergeCallException : Exception()
class CantLeaveConferenceException : Exception()

class Call(telecomCall: android.telecom.Call) : BaseObservable<Call.Listener>() {
    interface Listener {
        fun onCallChanged(call: Call)
    }

    private var _phoneAccountSelected: Boolean = false
    private val _id: String = Call::class.java.simpleName + sIdCounter++
    private val _call: android.telecom.Call = telecomCall

    init {
        _call.registerCallback(object : android.telecom.Call.Callback() {
            override fun onStateChanged(call: android.telecom.Call?, state: Int) {
                invokeListeners { it.onCallChanged(this@Call) }
            }

            override fun onDetailsChanged(call: android.telecom.Call?, details: Details?) {
                invokeListeners { it.onCallChanged(this@Call) }
            }

            override fun onConferenceableCallsChanged(
                call: android.telecom.Call?,
                conferenceableCalls: MutableList<android.telecom.Call>?
            ) {
                invokeListeners { it.onCallChanged(this@Call) }
            }
        })
    }

    val id: String
        get() = _id

    val handle: Uri?
        get() = details.handle

    val state: State
        get() = State.fromTelecomState(
            if (SDK_INT >= Build.VERSION_CODES.S) {
                details.state
            } else {
                _call.state
            }
        )

    val number: String?
        get() = if (details.gatewayInfo != null) {
            details.gatewayInfo.originalAddress.schemeSpecificPart
        } else {
            handle?.schemeSpecificPart
        }

    val extras: Bundle
        get() = details.extras

    val durationTimeMilis: Long
        get() = System.currentTimeMillis() - connectTimeMilis

    val connectTimeMilis: Long
        get() = details.connectTimeMillis

    val parentCall: Call?
        get() = if (_call.parent != null) Call(_call.parent) else null

    val callSubject: String?
        get() = if (extras.containsKey(Connection.EXTRA_CALL_SUBJECT)) {
            extras.getString(Connection.EXTRA_CALL_SUBJECT)
        } else {
            null
        }

    val isEnterprise: Boolean
        get() = details.hasProperty(PROPERTY_ENTERPRISE_CALL)

    val phoneAccountSelected: Boolean
        get() = _phoneAccountSelected

    val conferenceableCalls: List<Call>
        get() = fromTelecomCalls(_call.conferenceableCalls)

    val disconnectCause: DisconnectCause
        get() = if (state == DISCONNECTED) {
            details.disconnectCause
        } else {
            DisconnectCause(DisconnectCause.UNKNOWN)
        }

    val cannedSmsResponses: List<String>
        get() = _call.cannedTextResponses

    val telecomCall: android.telecom.Call
        get() = _call

    val details: Details
        get() = _call.details

    val phoneAccountHandle: PhoneAccountHandle
        get() = details.accountHandle

    val availablePhoneAccounts: List<PhoneAccountHandle>
        get() = intentExtras.getParcelableArrayList(AVAILABLE_PHONE_ACCOUNTS) ?: emptyList()

    val suggestedPhoneAccounts: List<PhoneAccountSuggestion>
        @RequiresApi(Q)
        get() = intentExtras.getParcelableArrayList(EXTRA_SUGGESTED_PHONE_ACCOUNTS) ?: emptyList()

    val intentExtras: Bundle
        get() = details.intentExtras

    val isActive: Boolean
        get() = state == ACTIVE

    val isStarted: Boolean
        get() = state !in arrayOf(DIALING, INCOMING, CONNECTING, NEW, SELECT_PHONE_ACCOUNT)

    val isHolding: Boolean
        get() = state == HOLDING

    val isIncoming: Boolean
        get() = state == INCOMING

    val children: List<Call>
        get() = _call.children.map(::Call).toList()

    val isConference: Boolean
        get() = details.hasProperty(Details.PROPERTY_CONFERENCE)

    val isInConference: Boolean
        get() = _call.parent != null

    val isConferenceParent: Boolean
        get() = _call.children.size > 0


    fun isCapable(capability: Int) =
        (details.callCapabilities and capability) != 0


    fun answer() {
        _call.answer(details.videoState)
    }

    /**
     * if its incoming, reject
     * else, disconnect
     */
    fun reject() {
        if (state == INCOMING) {
            if (SDK_INT >= Build.VERSION_CODES.R) {
                _call.reject(REJECT_REASON_DECLINED)
            } else {
                _call.reject(false, "")
            }
        } else {
            _call.disconnect()
        }
    }

    fun invokeKey(c: Char) {
        _call.playDtmfTone(c)
        _call.stopDtmfTone()
    }

    fun hold() {
        if (!isCapable(CAPABILITY_HOLD)) {
            throw CantHoldCallException()
        }
        _call.hold()
    }

    fun unHold() {
        _call.unhold()
    }


    /**
     * Merge the call
     * If there are conferenceable calls,
     * make the first conferenceable call (probably the first call in this session)
     * conference the new call (which will add the new call to existing conference if exist)
     * Otherwise, if call is capable of "merge conference", do it
     * Otherwise, throw an exception saying call cant be merge
     *
     * @throws CantMergeCallException
     */
    fun merge() {
        val conferenceableCalls = _call.conferenceableCalls
        when {
            conferenceableCalls.isNotEmpty() -> conferenceableCalls[0].conference(_call)
            isCapable(Connection.CAPABILITY_MERGE_CONFERENCE) -> _call.mergeConference()
            else -> throw CantMergeCallException()
        }
    }

    /**
     * Usually not capable
     * Swaps between foreground call and backend call
     *
     * @throws CantSwapCallException
     */
    fun swapConference() {
        if (!isCapable(Connection.CAPABILITY_SWAP_CONFERENCE)) {
            throw CantSwapCallException()
        }
        _call.swapConference()
    }

    fun leaveConference() {
        if (!isCapable(Connection.CAPABILITY_SEPARATE_FROM_CONFERENCE)) {
            throw CantLeaveConferenceException()
        }
        _call.splitFromConference()
    }

    fun joinConference(otherCall: Call) {
        _call.conference(otherCall.telecomCall)
    }

    fun selectPhoneAccount(accountHandle: PhoneAccountHandle) {
        _call.phoneAccountSelected(accountHandle, false)
        _phoneAccountSelected = true
    }


    override fun equals(other: Any?): Boolean {
        return other is Call && telecomCall == other.telecomCall
    }

    override fun hashCode(): Int {
        var result = _id.hashCode()
        result = 31 * result + _call.hashCode()
        return result
    }


    enum class State(private val state: Int, val stringRes: Int) {
        UNKNOWN(-1, R.string.call_status_unknown),
        NEW(STATE_NEW, R.string.call_status_new),
        ACTIVE(STATE_ACTIVE, R.string.call_status_active),
        HOLDING(STATE_HOLDING, R.string.call_status_holding),
        DIALING(STATE_DIALING, R.string.call_status_dialing),
        INCOMING(STATE_RINGING, R.string.call_status_incoming),
        CONNECTING(STATE_CONNECTING, R.string.call_status_connecting),
        PULLING_CALL(STATE_PULLING_CALL, R.string.call_status_pulling_call),
        DISCONNECTED(STATE_DISCONNECTED, R.string.call_status_disconnected),
        DISCONNECTING(STATE_DISCONNECTING, R.string.call_status_disconnecting),
        SELECT_PHONE_ACCOUNT(STATE_SELECT_PHONE_ACCOUNT, R.string.call_status_phone_account);

        companion object {
            fun fromTelecomState(state: Int): State =
                Stream.of(*values()).filter { callState: State -> callState.state == state }
                    .findFirst().orElse(UNKNOWN)
        }
    }

    companion object {
        private var sIdCounter = 0

        fun fromTelecomCalls(telecomCalls: List<android.telecom.Call>): List<Call> =
            telecomCalls.stream().map(::Call).collect(Collectors.toList())
    }
}