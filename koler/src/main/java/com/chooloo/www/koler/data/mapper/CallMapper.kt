package com.chooloo.www.koler.data.mapper

import android.os.Build
import android.telecom.Call as TelecomCall
import android.telecom.Call.Details
import android.telecom.CallAudioState
import android.telecom.PhoneAccountHandle
import android.telecom.TelecomManager
import androidx.core.os.BundleCompat
import com.chooloo.www.koler.domain.enum.AudioRoute
import com.chooloo.www.koler.domain.enum.CallCapability
import com.chooloo.www.koler.domain.enum.CallState
import com.chooloo.www.koler.domain.model.Call
import com.chooloo.www.koler.domain.model.CallAudio
import com.chooloo.www.koler.domain.model.PhoneAccount
import javax.inject.Inject

class CallMapper @Inject constructor(private val telecomManager: TelecomManager) {
    fun toCall(call: TelecomCall, id: String): Call {
        val details = call.details
        val state = telecomState(call)
        return Call(
            id = id,
            state = if (call.parent != null) {
                CallState.CONFERENCED
            } else {
                when (state) {
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
            },
            number = details.gatewayInfo?.originalAddress?.schemeSpecificPart
                ?: details.handle?.schemeSpecificPart,
            displayName = details.callerDisplayName?.takeIf { it.isNotEmpty() },
            isConference = details.hasProperty(Details.PROPERTY_CONFERENCE),
            connectTimeMillis = details.connectTimeMillis,
            capabilities = buildSet {
                if (details.can(Details.CAPABILITY_MUTE)) add(CallCapability.MUTE)
                if (details.can(Details.CAPABILITY_HOLD)) add(CallCapability.HOLD)
                if (details.can(Details.CAPABILITY_SWAP_CONFERENCE)) add(CallCapability.SWAP)
                if (details.can(Details.CAPABILITY_SEPARATE_FROM_CONFERENCE)) {
                    add(CallCapability.SEPARATE)
                }
                if (call.conferenceableCalls.isNotEmpty() ||
                    details.can(Details.CAPABILITY_MERGE_CONFERENCE)
                ) {
                    add(CallCapability.MERGE)
                }
            },
            phoneAccounts = if (state == TelecomCall.STATE_SELECT_PHONE_ACCOUNT) {
                availablePhoneAccounts(call).map { handle ->
                    val label = telecomManager.getPhoneAccount(handle)?.label?.toString()
                    PhoneAccount(accountId(handle), label ?: handle.id)
                }
            } else {
                emptyList()
            },
        )
    }

    fun toCallAudio(audioState: CallAudioState): CallAudio = CallAudio(
        isMuted = audioState.isMuted,
        route = AudioRoute.entries.firstOrNull { toTelecomRoute(it) == audioState.route },
        supportedRoutes = AudioRoute.entries.filter {
            audioState.supportedRouteMask and toTelecomRoute(it) == toTelecomRoute(it)
        },
    )

    fun toTelecomRoute(route: AudioRoute): Int = when (route) {
        AudioRoute.EARPIECE -> CallAudioState.ROUTE_EARPIECE
        AudioRoute.SPEAKER -> CallAudioState.ROUTE_SPEAKER
        AudioRoute.BLUETOOTH -> CallAudioState.ROUTE_BLUETOOTH
        AudioRoute.WIRED_HEADSET -> CallAudioState.ROUTE_WIRED_HEADSET
        AudioRoute.WIRED_OR_EARPIECE -> CallAudioState.ROUTE_WIRED_OR_EARPIECE
    }

    fun telecomState(call: TelecomCall): Int = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        call.details.state
    } else {
        @Suppress("DEPRECATION")
        call.state
    }

    fun availablePhoneAccounts(call: TelecomCall): List<PhoneAccountHandle> =
        call.details.intentExtras
            ?.let {
                BundleCompat.getParcelableArrayList(
                    it,
                    TelecomCall.AVAILABLE_PHONE_ACCOUNTS,
                    PhoneAccountHandle::class.java,
                )
            }
            .orEmpty()

    fun accountId(handle: PhoneAccountHandle): String =
        "${handle.componentName.flattenToString()}/${handle.id}"
}
