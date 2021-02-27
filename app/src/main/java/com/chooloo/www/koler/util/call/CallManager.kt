package com.chooloo.www.koler.util.call

import android.content.Context
import android.telecom.Call
import android.telecom.VideoProfile
import com.chooloo.www.koler.data.Contact
import com.chooloo.www.koler.util.lookupContact
import java.net.URLDecoder

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

    fun getContact(context: Context): Contact {
        return try {
            val number =
                URLDecoder.decode(sCall?.details?.handle.toString(), "utf-8").replace("tel:", "")
            if (number.contains("voicemail")) {
                Contact.VOICEMAIL
            } else {
                context.lookupContact(number)
            }
        } catch (e: Exception) {
            Contact.UNKNOWN
        }
    }
}