package com.chooloo.www.koler.data

import android.content.Context
import android.telecom.Call
import com.chooloo.www.koler.util.call.getCallerContact

data class CallDetails(
    val callState: CallState,
    val contact: Contact
) {
    enum class CallState(val state: Int) {
        ACTIVE(Call.STATE_ACTIVE),
        CONNECTING(Call.STATE_CONNECTING),
        DIALING(Call.STATE_DIALING),
        DISCONNECTED(Call.STATE_DISCONNECTED),
        DISCONNECTING(Call.STATE_DISCONNECTING),
        HOLDING(Call.STATE_HOLDING),
        NEW(Call.STATE_NEW),
        PULLING_CALL(Call.STATE_PULLING_CALL),
        RINGING(Call.STATE_RINGING),
        SELECT_PHONE_ACCOUNT(Call.STATE_SELECT_PHONE_ACCOUNT),
        UNKNOWN(-1)
    }

    companion object {
        fun fromCall(call: Call, context: Context) = CallDetails(
            callState = CallState.values().associateBy(CallState::state)
                .getOrDefault(call.state, CallState.UNKNOWN),
            contact = call.getCallerContact(context)
        )
    }
}