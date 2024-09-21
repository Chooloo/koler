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
import com.chooloo.www.chooloolib.domain.model.ICallData.CallException

class CallData(
    override val call: Call
) : ICallData {
    override val handle: Uri?
        get() = call.details.handle

    override val extras: Bundle
        get() = call.details.extras ?: Bundle()

    override val canMute: Boolean
        get() = isCapable(CAPABILITY_MUTE)

    override val canHold: Boolean
        get() = isCapable(CAPABILITY_HOLD)

    override val canSwap: Boolean
        get() = isCapable(CAPABILITY_SWAP_CONFERENCE)

    override val parentCall: ICallData?
        get() = call.parent?.let(::CallData)

    override val isHolding: Boolean
        get() = state == CallState.HOLDING

    override val isDialing: Boolean
        get() = state in arrayOf(CallState.DIALING, CallState.REDIALING)

    @Suppress("DEPRECATION")
    override val state: CallState
        get() = parentCall?.run {
            CallState.CONFERENCED
        } ?: CallState.fromTelecomState(
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                call.details.state
            } else {
                call.state
            }
        )

    override val subject: String?
        get() = if (extras.containsKey(Connection.EXTRA_CALL_SUBJECT)) {
            extras.getString(Connection.EXTRA_CALL_SUBJECT)
        } else {
            null
        }

    override val number: String?
        get() = if (call.details.gatewayInfo != null) {
            call.details.gatewayInfo.originalAddress.schemeSpecificPart
        } else {
            handle?.schemeSpecificPart
        }

    override val isConference: Boolean
        get() = call.details.hasProperty(Details.PROPERTY_CONFERENCE)

    override val isEnterprise: Boolean
        get() = call.details.hasProperty(Details.PROPERTY_ENTERPRISE_CALL)

    override val connectMillis: Long
        get() = call.details.connectTimeMillis

    override val durationMillis: Long
        get() = System.currentTimeMillis() - connectMillis

    override val callerDisplayName: String
        get() = call.details.callerDisplayName

    override val textResponses: List<String>
        get() = call.cannedTextResponses

    override val childrenCalls: List<ICallData>
        get() = call.children.map(::CallData)

    override val conferenceableCalls: List<ICallData>
        get() = call.conferenceableCalls.map(::CallData)

    @Suppress("DEPRECATION")
    override val suggestedPhoneAccounts: List<PhoneAccountSuggestion>
        @RequiresApi(Build.VERSION_CODES.Q) get() = (
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                    call.details.intentExtras.getParcelableArrayList(
                        EXTRA_SUGGESTED_PHONE_ACCOUNTS,
                        PhoneAccountSuggestion::class.java
                    )
                else call.details.intentExtras.getParcelableArrayList(
                    EXTRA_SUGGESTED_PHONE_ACCOUNTS
                )
                ) ?: emptyList()

    @Suppress("DEPRECATION")
    override val availablePhoneAccounts: List<PhoneAccountHandle>
        get() = (
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                    call.details.intentExtras.getParcelableArrayList(
                        AVAILABLE_PHONE_ACCOUNTS,
                        PhoneAccountHandle::class.java
                    )
                else
                    call.details.intentExtras.getParcelableArrayList(AVAILABLE_PHONE_ACCOUNTS)
                )
            ?: emptyList()

    override val isConnectingOrConnected: Boolean
        get() = state in arrayOf(
            CallState.ACTIVE,
            CallState.HOLDING,
            CallState.CALL_WAITING,
            CallState.CONNECTING,
            CallState.DIALING,
            CallState.REDIALING,
            CallState.HOLDING,
            CallState.CONFERENCED
        )

    override val disconnectCause: DisconnectCause
        get() = if (state in arrayOf(CallState.DISCONNECTED, CallState.IDLE)) {
            call.details.disconnectCause
        } else {
            DisconnectCause(DisconnectCause.UNKNOWN)
        }

    override val hasValidAccounts: Boolean
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
            return !(call.details.accountHandle == null && phoneAccountHandles == null || phoneAccountHandles!!.isEmpty())
        }

    override val hasStarted: Boolean
        get() = state !in arrayOf(
            CallState.NEW,
            CallState.DIALING,
            CallState.INCOMING,
            CallState.CONNECTING,
            CallState.SELECT_PHONE_ACCOUNT
        )

    override fun isCapable(capability: Int) = (call.details.callCapabilities and capability) != 0

    /**
     * @throws CallException.CantHoldCallException
     */
    override fun hold() {
        if (!isCapable(Details.CAPABILITY_HOLD)) {
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
    override fun mergeConference() {
        val conferenceableCalls = call.conferenceableCalls
        when {
            conferenceableCalls.isNotEmpty() -> conferenceableCalls[0].conference(call)
            isCapable(Connection.CAPABILITY_MERGE_CONFERENCE) -> call.mergeConference()
            else -> throw CallException.CantMergeCallException()
        }
    }

    override fun unhold() {
        call.unhold()
    }

    override fun accept() {
        call.answer(call.details.videoState)
    }

    override fun decline(message: String?) {
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

    /**
     * Usually not capable
     * Swaps between foreground call and backend call
     *
     * @throws CallException.CantSwapCallException
     */
    override fun swapConference() {
        if (!isCapable(Connection.CAPABILITY_SWAP_CONFERENCE)) {
            throw CallException.CantSwapCallException()
        }
        call.swapConference()
    }

    /**
     * @throws CallException.CantLeaveConferenceException
     */
    override fun leaveConference() {
        if (!isCapable(Connection.CAPABILITY_SEPARATE_FROM_CONFERENCE)) {
            throw CallException.CantLeaveConferenceException()
        }
        call.splitFromConference()
    }

    override fun conference(call: ICallData) {
        this.call.conference(call.call)
    }

    override fun playKey(c: Char) {
        call.playDtmfTone(c)
        call.stopDtmfTone()
    }

    override fun selectAccount(accountHandle: PhoneAccountHandle) {
        call.phoneAccountSelected(accountHandle, false)
    }
}
