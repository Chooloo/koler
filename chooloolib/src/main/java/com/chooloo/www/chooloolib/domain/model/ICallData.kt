package com.chooloo.www.chooloolib.domain.model

import android.net.Uri
import android.os.Bundle
import android.telecom.Call
import android.telecom.DisconnectCause
import android.telecom.PhoneAccountHandle
import android.telecom.PhoneAccountSuggestion

interface ICallData {
    val call: Call
    val state: CallState
    val handle: Uri?
    val extras: Bundle
    val canMute: Boolean
    val canHold: Boolean
    val canSwap: Boolean
    val number: String?
    val subject: String?
    val parentCall: ICallData?
    val isHolding: Boolean
    val isDialing: Boolean
    val hasStarted: Boolean
    val isConference: Boolean
    val isEnterprise: Boolean
    val textResponses: List<String>
    val childrenCalls: List<ICallData>
    val connectMillis: Long
    val durationMillis: Long
    val disconnectCause: DisconnectCause
    val hasValidAccounts: Boolean
    val callerDisplayName: String?
    val conferenceableCalls: List<ICallData>
    val suggestedPhoneAccounts: List<PhoneAccountSuggestion>
    val availablePhoneAccounts: List<PhoneAccountHandle>
    val isConnectingOrConnected: Boolean

    fun isCapable(capability: Int): Boolean
    fun hold()
    fun unhold()
    fun accept()
    fun decline(message: String? = null)
    fun swapConference()
    fun mergeConference()
    fun leaveConference()
    fun conference(call: ICallData)
    fun playKey(c: Char)
    fun selectAccount(accountHandle: PhoneAccountHandle)

    sealed class CallException : Exception() {
        class CantHoldCallException : CallException()
        class CantSwapCallException : CallException()
        class CantMergeCallException : CallException()
        class CantLeaveConferenceException : CallException()
    }
}