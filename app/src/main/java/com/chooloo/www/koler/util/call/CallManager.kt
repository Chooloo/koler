package com.chooloo.www.koler.util.call

import android.content.Context
import android.telecom.Call
import android.telecom.VideoProfile
import android.telephony.PhoneNumberUtils


object CallManager {
    var sCall: Call? = null

    val number: String?
        get() = sCall?.details?.gatewayInfo?.originalAddress?.schemeSpecificPart
            ?: sCall?.details?.handle?.schemeSpecificPart

    fun answer() {
        sCall?.answer(VideoProfile.STATE_AUDIO_ONLY)
    }

    fun reject() {
        if (sCall?.state == Call.STATE_RINGING) {
            sCall?.reject(false, null)
        } else {
            sCall?.disconnect()
        }
    }

    fun hold(isHold: Boolean) {
        if (isHold) {
            sCall?.hold()
        } else {
            sCall?.unhold()
        }
    }

    fun keypad(c: Char) {
        sCall?.playDtmfTone(c)
        sCall?.stopDtmfTone()
    }

    fun registerCallback(callback: Call.Callback) {
        sCall?.registerCallback(callback)
    }

    fun unregisterCallback(callback: Call.Callback) {
        sCall?.unregisterCallback(callback)
    }

    fun getValidE164Number(context: Context) =
        PhoneNumberUtils.formatNumberToE164(number, context.resources.configuration.locale.country)

    fun getNormalizedNumber(context: Context) =
        PhoneNumberUtils.normalizeNumber(getValidE164Number(context))
}