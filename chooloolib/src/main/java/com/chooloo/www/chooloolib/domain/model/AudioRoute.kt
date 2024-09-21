package com.chooloo.www.chooloolib.domain.model

import android.telecom.CallAudioState
import androidx.annotation.StringRes
import com.chooloo.www.chooloolib.R

enum class AudioRoute(
    val route: Int,
    @StringRes val stringRes: Int
) {
    SPEAKER(CallAudioState.ROUTE_SPEAKER, R.string.audio_route_speaker),
    EARPIECE(CallAudioState.ROUTE_EARPIECE, R.string.audio_route_earpiece),
    BLUETOOTH(CallAudioState.ROUTE_BLUETOOTH, R.string.audio_route_bluetooth),
    WIRED_HEADSET(CallAudioState.ROUTE_WIRED_HEADSET, R.string.audio_route_wired_headset),
    WIRED_OR_EARPIECE(
        CallAudioState.ROUTE_WIRED_OR_EARPIECE,
        R.string.audio_route_wired_or_earpiece
    );

    companion object {
        fun fromRoute(route: Int?) = entries.firstOrNull { it.route == route }
    }
}