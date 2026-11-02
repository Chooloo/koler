package com.chooloo.www.koler.ui.call

import android.content.Context
import androidx.annotation.StringRes
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.domain.model.PhoneLookup
import com.chooloo.www.koler.domain.AudioRoute
import com.chooloo.www.koler.domain.Call
import com.chooloo.www.koler.domain.CallState

@get:StringRes
val CallState.labelRes: Int
    get() = when (this) {
        CallState.CONNECTING -> R.string.call_state_connecting
        CallState.DIALING -> R.string.call_state_dialing
        CallState.INCOMING -> R.string.call_state_incoming
        CallState.ACTIVE -> R.string.call_state_active
        CallState.HOLDING -> R.string.call_state_holding
        CallState.CONFERENCED -> R.string.call_state_conferenced
        CallState.DISCONNECTING -> R.string.call_state_disconnecting
        CallState.DISCONNECTED -> R.string.call_state_disconnected
        CallState.SELECT_PHONE_ACCOUNT -> R.string.call_state_phone_account
        CallState.UNKNOWN -> R.string.call_state_unknown
    }

@get:StringRes
val AudioRoute.labelRes: Int
    get() = when (this) {
        AudioRoute.EARPIECE -> R.string.audio_route_earpiece
        AudioRoute.SPEAKER -> R.string.audio_route_speaker
        AudioRoute.BLUETOOTH -> R.string.audio_route_bluetooth
        AudioRoute.WIRED_HEADSET -> R.string.audio_route_wired_headset
        AudioRoute.WIRED_OR_EARPIECE -> R.string.audio_route_wired_or_earpiece
    }

fun Call.title(context: Context, caller: PhoneLookup?): String = when {
    isConference -> context.getString(R.string.conference)
    else -> caller?.name ?: displayName ?: number ?: context.getString(R.string.caller_unknown)
}
