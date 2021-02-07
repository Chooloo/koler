package com.chooloo.www.koler.service

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.telecom.Call
import android.telecom.VideoProfile
import android.widget.Toast
import com.chooloo.www.koler.entity.Contact
import com.chooloo.www.koler.ui.base.BaseActivity
import com.chooloo.www.koler.util.isDefaultDialer
import com.chooloo.www.koler.util.lookupContact
import com.chooloo.www.koler.util.requestDefaultDialer
import timber.log.Timber
import java.net.URLDecoder

object CallManager {
    @JvmField var sCall: Call? = null
    @JvmField val state: Int = sCall?.state ?: Call.STATE_CONNECTING

    @JvmStatic
    fun answer() {
        sCall?.answer(VideoProfile.STATE_AUDIO_ONLY)
    }

    @JvmStatic
    fun reject() {
        if (sCall?.state == Call.STATE_RINGING) {
            sCall?.reject(false, null)
        } else {
            sCall?.disconnect()
        }
    }

    @JvmStatic
    fun hold(isHold: Boolean) {
        if (isHold) sCall?.hold() else sCall?.unhold()
    }

    @JvmStatic
    fun keypad(c: Char) {
        sCall?.playDtmfTone(c)
        sCall?.stopDtmfTone()
    }

    @JvmStatic
    fun registerCallback(callback: Call.Callback?) {
        sCall?.registerCallback(callback)
    }

    @JvmStatic
    fun unregisterCallback(callback: Call.Callback?) {
        sCall?.unregisterCallback(callback)
    }

    @JvmStatic
    fun call(activity: BaseActivity, number: String?) {
        if (activity.isDefaultDialer()) {
            val callIntent = Intent(Intent.ACTION_CALL, Uri.parse("tel:" + Uri.encode(number)))
            activity.startActivity(callIntent)
        } else {
            activity.requestDefaultDialer()
            activity.showError("Set Koler as your default dialer to make calls")
        }
    }

    @JvmStatic
    fun callVoicemail(context: Context) {
        try {
            val intent = Intent(Intent.ACTION_CALL, Uri.parse("voicemail:1"))
            context.startActivity(intent)
        } catch (e: SecurityException) {
            Toast.makeText(context, "Couldn't start Voicemail", Toast.LENGTH_LONG).show()
            Timber.e(e)
        }
    }

    @JvmStatic
    fun getContact(context: Context): Contact {
        return try {
            val number = URLDecoder.decode(sCall?.details?.handle.toString(), "utf-8").replace("tel:", "")
            if (number.contains("voicemail")) {
                Contact.VOICEMAIL
            } else {
                lookupContact(context, number)
            }
        } catch (e: Exception) {
            Contact.UNKNOWN
        }
    }
}