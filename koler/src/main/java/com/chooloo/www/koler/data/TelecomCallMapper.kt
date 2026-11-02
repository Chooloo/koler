package com.chooloo.www.koler.data

import android.os.Build
import android.telecom.Call as TelecomCall
import android.telecom.Call.Details
import android.telecom.CallAudioState
import com.chooloo.www.koler.domain.AudioRoute
import com.chooloo.www.koler.domain.Call
import com.chooloo.www.koler.domain.CallAudio
import com.chooloo.www.koler.domain.CallState

fun TelecomCall.toDomain(id: String) = Call(
    id = id,
    state = if (parent != null) CallState.CONFERENCED else callStateOf(telecomState),
    number = details.gatewayInfo?.originalAddress?.schemeSpecificPart ?: details.handle?.schemeSpecificPart,
    displayName = details.callerDisplayName?.takeIf { it.isNotEmpty() },
    isConference = details.hasProperty(Details.PROPERTY_CONFERENCE),
    connectTimeMillis = details.connectTimeMillis,
    capabilities = buildSet {
        if (details.can(Details.CAPABILITY_MUTE)) add(Call.Capability.MUTE)
        if (details.can(Details.CAPABILITY_HOLD)) add(Call.Capability.HOLD)
        if (details.can(Details.CAPABILITY_SWAP_CONFERENCE)) add(Call.Capability.SWAP)
        if (details.can(Details.CAPABILITY_SEPARATE_FROM_CONFERENCE)) add(Call.Capability.SEPARATE)
        if (conferenceableCalls.isNotEmpty() || details.can(Details.CAPABILITY_MERGE_CONFERENCE)) {
            add(Call.Capability.MERGE)
        }
    },
)

val TelecomCall.telecomState: Int
    @Suppress("DEPRECATION")
    get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) details.state else state

fun CallAudioState.toDomain() = CallAudio(
    isMuted = isMuted,
    route = audioRouteOf(route),
    supportedRoutes = AudioRoute.entries.filter { supportedRouteMask and it.telecomRoute == it.telecomRoute },
)

val AudioRoute.telecomRoute: Int
    get() = when (this) {
        AudioRoute.EARPIECE -> CallAudioState.ROUTE_EARPIECE
        AudioRoute.SPEAKER -> CallAudioState.ROUTE_SPEAKER
        AudioRoute.BLUETOOTH -> CallAudioState.ROUTE_BLUETOOTH
        AudioRoute.WIRED_HEADSET -> CallAudioState.ROUTE_WIRED_HEADSET
        AudioRoute.WIRED_OR_EARPIECE -> CallAudioState.ROUTE_WIRED_OR_EARPIECE
    }

private fun audioRouteOf(route: Int) = AudioRoute.entries.firstOrNull { it.telecomRoute == route }

private fun callStateOf(state: Int) = when (state) {
    TelecomCall.STATE_NEW, TelecomCall.STATE_CONNECTING -> CallState.CONNECTING
    TelecomCall.STATE_DIALING -> CallState.DIALING
    TelecomCall.STATE_RINGING -> CallState.INCOMING
    TelecomCall.STATE_ACTIVE -> CallState.ACTIVE
    TelecomCall.STATE_HOLDING -> CallState.HOLDING
    TelecomCall.STATE_DISCONNECTING -> CallState.DISCONNECTING
    TelecomCall.STATE_DISCONNECTED -> CallState.DISCONNECTED
    TelecomCall.STATE_SELECT_PHONE_ACCOUNT -> CallState.SELECT_PHONE_ACCOUNT
    else -> CallState.UNKNOWN
}
