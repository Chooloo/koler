package com.chooloo.www.koler.call

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.telecom.Call
import android.telecom.VideoProfile
import android.widget.Toast
import com.chooloo.www.koler.KolerApp
import com.chooloo.www.koler.R
import com.chooloo.www.koler.data.CallDetails


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

    fun registerListener(callListener: CallListener) {
        sCall?.registerCallback(callListener)
        sCall?.let {
            callListener.onStateChanged(it, it.state)
            callListener.onDetailsChanged(it, it.details)
        }
    }

    fun unregisterCallback(callListener: CallListener) {
        sCall?.unregisterCallback(callListener)
    }

    fun call(context: Context, number: String) {
        (context.applicationContext as KolerApp).componentRoot.permissionInteractor.runWithDefaultDialer(
            R.string.error_not_default_dialer_call
        ) {
            val callIntent = Intent(Intent.ACTION_CALL)
            callIntent.data = Uri.parse("tel:${Uri.encode(number)}")
            context.startActivity(callIntent)
        }
    }

    fun callVoicemail(context: Context) {
        try {
            val intent = Intent(Intent.ACTION_CALL, Uri.parse("voicemail:1"))
            context.startActivity(intent)
        } catch (e: SecurityException) {
            Toast.makeText(context, "Couldn't start voicemail, no permissions", Toast.LENGTH_SHORT)
                .show()
        }
    }

    abstract class CallListener(protected val context: Context) : Call.Callback() {
        override fun onStateChanged(call: Call, state: Int) {
            onCallDetailsChanged(CallDetails.fromCall(call, context))
        }

        override fun onDetailsChanged(call: Call, details: Call.Details) {
            onCallDetailsChanged(CallDetails.fromCall(call, context))
        }

        abstract fun onCallDetailsChanged(callDetails: CallDetails)
    }
}