package com.chooloo.www.koler.ui.common

import android.content.Context
import androidx.annotation.StringRes
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.domain.model.PhoneLookup
import com.chooloo.www.koler.domain.enum.AudioRoute
import com.chooloo.www.koler.domain.enum.CallState
import com.chooloo.www.koler.domain.model.Call

object CallLabels {
    @StringRes
    fun labelRes(state: CallState): Int = when (state) {
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

    @StringRes
    fun labelRes(route: AudioRoute): Int = when (route) {
        AudioRoute.EARPIECE -> R.string.audio_route_earpiece
        AudioRoute.SPEAKER -> R.string.audio_route_speaker
        AudioRoute.BLUETOOTH -> R.string.audio_route_bluetooth
        AudioRoute.WIRED_HEADSET -> R.string.audio_route_wired_headset
        AudioRoute.WIRED_OR_EARPIECE -> R.string.audio_route_wired_or_earpiece
    }

    fun title(call: Call, context: Context, caller: PhoneLookup?): String = when {
        call.isConference -> context.getString(R.string.conference)

        else ->
            caller?.name ?: call.displayName ?: call.number
                ?: context.getString(R.string.caller_unknown)
    }
}
