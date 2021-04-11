package com.chooloo.www.koler.util.call

import android.content.Context
import android.telecom.Call
import android.telecom.VideoProfile
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