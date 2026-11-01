package com.chooloo.www.chooloolib.domain.model

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.telecom.Call
import android.telecom.Call.AVAILABLE_PHONE_ACCOUNTS
import android.telecom.Call.Details
import android.telecom.Call.Details.CAPABILITY_HOLD
import android.telecom.Call.Details.CAPABILITY_MUTE
import android.telecom.Call.Details.CAPABILITY_SWAP_CONFERENCE
import android.telecom.Call.EXTRA_SUGGESTED_PHONE_ACCOUNTS
import android.telecom.Connection
import android.telecom.DisconnectCause
import android.telecom.PhoneAccountHandle
import android.telecom.PhoneAccountSuggestion
import androidx.annotation.RequiresApi

class CallData(
    val call: Call
) {
    val handle: Uri?
        get() = call.details.handle

    val extras: Bundle
        get() = call.details.extras ?: Bundle()

    val canMute: Boolean
        get() = isCapable(CAPABILITY_MUTE)

    val canHold: Boolean
        get() = isCapable(CAPABILITY_HOLD)

    val canSwap: Boolean
        get() = isCapable(CAPABILITY_SWAP_CONFERENCE)

    val parentCall: CallData?
        get() = call.parent?.let(::CallData)

    val isHolding: Boolean
        get() = state == CallState.HOLDING

    val isDialing: Boolean
        get() = state in arrayOf(CallState.DIALING, CallState.REDIALING)

    @Suppress("DEPRECATION")
    val state: CallState
        get() = parentCall?.run {
            CallState.CONFERENCED
        } ?: CallState.fromTelecomState(
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                call.details.state
            } else {
                call.state
            }
        )

    val subject: String?
        get() = if (extras.containsKey(Connection.EXTRA_CALL_SUBJECT)) {
            extras.getString(Connection.EXTRA_CALL_SUBJECT)
        } else {
            null
        }

    val number: String?
        get() = if (call.details.gatewayInfo != null) {
            call.details.gatewayInfo.originalAddress.schemeSpecificPart
        } else {
            handle?.schemeSpecificPart
        }

    val isConference: Boolean
        get() = call.details.hasProperty(Details.PROPERTY_CONFERENCE)

    val isEnterprise: Boolean
        get() = call.details.hasProperty(Details.PROPERTY_ENTERPRISE_CALL)

    val connectMillis: Long
        get() = call.details.connectTimeMillis

    val durationMillis: Long
        get() = if (connectMillis > 0) System.currentTimeMillis() - connectMillis else 0L

    val callerDisplayName: String?
        get() = call.details.callerDisplayName

    val textResponses: List<String>
        get() = call.cannedTextResponses

    val childrenCalls: List<CallData>
        get() = call.children.map(::CallData)

    val conferenceableCalls: List<CallData>
        get() = call.conferenceableCalls.map(::CallData)

    @Suppress("DEPRECATION")
    val suggestedPhoneAccounts: List<PhoneAccountSuggestion>
        @RequiresApi(Build.VERSION_CODES.Q) get() = (
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                    call.details.intentExtras?.getParcelableArrayList(
                        EXTRA_SUGGESTED_PHONE_ACCOUNTS,
                        PhoneAccountSuggestion::class.java
                    )
                else call.details.intentExtras?.getParcelableArrayList(
                    EXTRA_SUGGESTED_PHONE_ACCOUNTS
                )
                ) ?: emptyList()

    @Suppress("DEPRECATION")
    val availablePhoneAccounts: List<PhoneAccountHandle>
        get() = (
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                    call.details.intentExtras?.getParcelableArrayList(
                        AVAILABLE_PHONE_ACCOUNTS,
                        PhoneAccountHandle::class.java
                    )
                else
                    call.details.intentExtras?.getParcelableArrayList(AVAILABLE_PHONE_ACCOUNTS)
                )
            ?: emptyList()

    val isConnectingOrConnected: Boolean
        get() = state in arrayOf(
            CallState.ACTIVE,
            CallState.HOLDING,
            CallState.CALL_WAITING,
            CallState.CONNECTING,
            CallState.DIALING,
            CallState.REDIALING,
            CallState.CONFERENCED
        )

    val disconnectCause: DisconnectCause
        get() = if (state in arrayOf(CallState.DISCONNECTED, CallState.IDLE)) {
            call.details.disconnectCause
        } else {
            DisconnectCause(DisconnectCause.UNKNOWN)
        }

    val hasValidAccounts: Boolean
        get() {
            @Suppress("DEPRECATION")
            val phoneAccountHandles =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                    extras.getParcelableArrayList(
                        EXTRA_SUGGESTED_PHONE_ACCOUNTS,
                        PhoneAccountHandle::class.java
                    )
                else
                    extras.getParcelableArrayList(AVAILABLE_PHONE_ACCOUNTS)
            return !(call.details.accountHandle == null && phoneAccountHandles.isNullOrEmpty())
        }

    val hasStarted: Boolean
        get() = state !in arrayOf(
            CallState.NEW,
            CallState.DIALING,
            CallState.INCOMING,
            CallState.CONNECTING,
            CallState.SELECT_PHONE_ACCOUNT
        )

    fun isCapable(capability: Int) = (call.details.callCapabilities and capability) != 0

    fun hold() {
        if (!isCapable(Details.CAPABILITY_HOLD)) {
            throw CallException.CantHoldCallException()
        }
        call.hold()
    }

    fun mergeConference() {
        val conferenceableCalls = call.conferenceableCalls
        when {
            conferenceableCalls.isNotEmpty() -> conferenceableCalls[0].conference(call)
            isCapable(Connection.CAPABILITY_MERGE_CONFERENCE) -> call.mergeConference()
            else -> throw CallException.CantMergeCallException()
        }
    }

    fun unhold() {
        call.unhold()
    }

    fun accept() {
        call.answer(call.details.videoState)
    }

    fun decline(message: String? = null) {
        if (state == CallState.INCOMING) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                call.reject(Call.REJECT_REASON_DECLINED)
            } else {
                call.reject(false, message)
            }
        } else {
            call.disconnect()
        }
    }

    fun swapConference() {
        if (!isCapable(Connection.CAPABILITY_SWAP_CONFERENCE)) {
            throw CallException.CantSwapCallException()
        }
        call.swapConference()
    }

    fun leaveConference() {
        if (!isCapable(Connection.CAPABILITY_SEPARATE_FROM_CONFERENCE)) {
            throw CallException.CantLeaveConferenceException()
        }
        call.splitFromConference()
    }

    fun conference(otherCall: CallData) {
        this.call.conference(otherCall.call)
    }

    fun playKey(c: Char) {
        call.playDtmfTone(c)
        call.stopDtmfTone()
    }

    fun selectAccount(accountHandle: PhoneAccountHandle) {
        call.phoneAccountSelected(accountHandle, false)
    }

    sealed class CallException : Exception() {
        class CantHoldCallException : CallException()
        class CantSwapCallException : CallException()
        class CantMergeCallException : CallException()
        class CantLeaveConferenceException : CallException()
    }
}
