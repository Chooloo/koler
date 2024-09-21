package com.chooloo.www.chooloolib.domain.model

import android.telecom.Call
import androidx.annotation.StringRes
import com.chooloo.www.chooloolib.R

enum class CallState(@StringRes val stringRes: Int) {
    NEW(R.string.call_state_new),
    IDLE(R.string.call_state_idle),
    ACTIVE(R.string.call_state_active),
    UNKNOWN(R.string.call_state_unknown),
    HOLDING(R.string.call_state_holding),
    DIALING(R.string.call_state_dialing),
    INCOMING(R.string.call_state_incoming),
    REDIALING(R.string.call_state_redialing),
    CONNECTING(R.string.call_state_connecting),
    CONFERENCED(R.string.call_state_conferenced),
    CALL_WAITING(R.string.call_state_call_waiting),
    DISCONNECTED(R.string.call_state_disconnected),
    DISCONNECTING(R.string.call_state_disconnecting),
    SELECT_PHONE_ACCOUNT(R.string.call_state_phone_account);

    companion object {
        fun fromTelecomState(state: Int): CallState =
            when (state) {
                Call.STATE_ACTIVE -> ACTIVE
                Call.STATE_DIALING -> DIALING
                Call.STATE_HOLDING -> HOLDING
                Call.STATE_RINGING -> INCOMING
                Call.STATE_DISCONNECTED -> DISCONNECTED
                Call.STATE_DISCONNECTING -> DISCONNECTING
                Call.STATE_NEW, Call.STATE_CONNECTING -> CONNECTING
                Call.STATE_SELECT_PHONE_ACCOUNT -> SELECT_PHONE_ACCOUNT
                else -> UNKNOWN
            }
    }
}