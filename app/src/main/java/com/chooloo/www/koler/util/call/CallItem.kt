package com.chooloo.www.koler.util.call

import android.content.Context
import android.os.Bundle
import android.provider.ContactsContract
import android.telecom.Call
import com.chooloo.www.koler.R
import com.chooloo.www.koler.data.PhoneLookupAccount
import com.chooloo.www.koler.util.call.CallItem.Companion.CallState.RINGING

class CallItem(
    private val _call: Call
) : Call.Callback() {
    companion object {
        enum class CallState(val state: Int, val stringRes: Int) {
            NEW(Call.STATE_NEW, R.string.call_status_new),
            UNKNOWN(-1, R.string.call_status_unknown),
            ACTIVE(Call.STATE_ACTIVE, R.string.call_status_active),
            DIALING(Call.STATE_DIALING, R.string.call_status_dialing),
            HOLDING(Call.STATE_HOLDING, R.string.call_status_holding),
            RINGING(Call.STATE_RINGING, R.string.call_status_incoming),
            CONNECTING(Call.STATE_CONNECTING, R.string.call_status_connecting),
            DISCONNECTED(Call.STATE_DISCONNECTED, R.string.call_status_disconnected),
            PULLING_CALL(Call.STATE_PULLING_CALL, R.string.call_status_pulling_call),
            DISCONNECTING(Call.STATE_DISCONNECTING, R.string.call_status_disconnecting),
            PHONE_ACCOUNT(Call.STATE_SELECT_PHONE_ACCOUNT, R.string.call_status_phone_account),
        }

        enum class CallCapability(
            private val value: Int
        ) {
            MUTE(0x00000040),
            HOLD(0x00000001),
            VT_LOCAL_RX(0x00000100),
            VT_LOCAL_TX(0x00000200),
            VT_REMOTE_RX(0x00000400),
            VT_REMOTE_TX(0x00000800),
            SUPPORT_HOLD(0x00000002),
            CAN_PAUSE_VIDEO(0x00100000),
            SWAP_CONFERENCE(0x00000008),
            MERGE_CONFERENCE(0x00000004),
            RESPOND_VIA_TEXT(0x00000020),
            MANAGE_CONFERENCE(0x00000080),
            SPEED_UP_MT_AUDIO(0x00040000),
            CAN_UPGRADE_TO_VIDEO(0x00080000),
            SEPARATE_FROM_CONFERENCE(0x00001000),
            DISCONNECT_FROM_CONFERENCE(0x00002000);

            fun isCapable(callItem: CallItem) =
                callItem.details.callCapabilities and value != 0
        }
    }

    private var _isMerging: Boolean = false

    init {
        _call.registerCallback(this)
    }

    // region details

    val details: Call.Details
        get() = _call.details

    val parentCall: CallItem
        get() = CallItem(_call.parent)

    val timeConnected: Long
        get() = if (_call.details.connectTimeMillis <= 0) {
            System.currentTimeMillis()
        } else {
            _call.details.connectTimeMillis
        }

    // endregion

    // region state

    val state: CallState
        get() = CallState.values().associateBy(CallState::state)
            .getOrDefault(_call.state, CallState.UNKNOWN)

    val isMerging: Boolean
        get() = _isMerging

    val isConnected: Boolean
        get() = state == CallState.ACTIVE

    val isDisconnected: Boolean
        get() = state in arrayOf(CallState.DISCONNECTING, CallState.DISCONNECTING)

    // endregion

    // region conference

    val isInConference: Boolean
        get() = _call.parent != null

    val isConferenceParent: Boolean
        get() = _call.children.size > 0

    // endregion

    // region capabilities

    val canLeaveConference: Boolean
        get() = CallCapability.SEPARATE_FROM_CONFERENCE.isCapable(this)

    val canDisconnectFromConference: Boolean
        get() = CallCapability.DISCONNECT_FROM_CONFERENCE.isCapable(this)

    val canHold: Boolean
        get() = CallCapability.HOLD.isCapable(this)

    val canSupportHold: Boolean
        get() = CallCapability.SUPPORT_HOLD.isCapable(this)

    // endregion

    // region call actions

    fun answer() {
        _call.answer(_call.details.videoState)
    }

    fun reject(message: String? = null) {
        if (state == RINGING) {
            _call.reject(message != null, message)
        } else {
            _call.disconnect()
        }
    }

    fun hold() {
        _call.hold()
        _call.children.forEach { it.hold() }
    }

    fun unhold() {
        _call.unhold()
    }

    fun conference(callItem: CallItem) {
        _call.conference(callItem._call)
    }

    fun swapConference() {
        _call.swapConference()
    }

    fun mergeConference() {
        _call.mergeConference()
    }

    fun leaveConference() {
        if (canLeaveConference) {
            _call.splitFromConference()
        }
    }

    fun invokeKeypadChar(char: Char) {
        _call.apply {
            playDtmfTone(char)
            stopDtmfTone()
        }
    }

    // endregion

    // region detail fetchers

    fun getAccount(context: Context) = _call.lookupContact(context) ?: PhoneLookupAccount(
        name = null,
        number = _call.getNumber(),
        type = ContactsContract.CommonDataKinds.Phone.TYPE_OTHER
    )

    fun isTheSameCall(callItem: CallItem?) =
        details.accountHandle.id == callItem?.details?.accountHandle?.id

    // endregion

    // region events

    fun unregisterListener(callCallback: Call.Callback) {
        _call.unregisterCallback(callCallback)
    }

    fun registerListener(callCallback: Call.Callback) {
        _call.registerCallback(callCallback)
    }

    override fun onCallDestroyed(call: Call?) {
        if (_call == call) {
            _call.unregisterCallback(this)
        }
    }

    override fun onConnectionEvent(call: Call?, event: String?, extras: Bundle?) {
        super.onConnectionEvent(call, event, extras)
        if (_call == call) {
            if (event?.contains("MERGE_START") == true) {
                _isMerging = true
            } else if (event?.contains("MERGE_COMPLETE") == true) {
                _isMerging = false
            }
        }
    }

    // endregion
}