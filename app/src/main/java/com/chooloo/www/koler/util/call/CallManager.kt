package com.chooloo.www.koler.util.call

import android.telecom.Call
import android.telecom.TelecomManager
import android.telecom.VideoProfile

object CallManager {
    var sCall: Call? = null

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

    fun getNumber(details: Call.Details?): String? {
        details?.let {
            for (key in arrayOf(TelecomManager.EXTRA_CALL_BACK_NUMBER, "oi")) {
                val value = details.extras?.getString(key)
                if (!value.isNullOrEmpty()) {
                    return value
                }
                details.handle?.let {
                    return it.schemeSpecificPart
                }
            }
        }
        return null
    }
}