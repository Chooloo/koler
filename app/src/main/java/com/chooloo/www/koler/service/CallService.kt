package com.chooloo.www.koler.service

import android.annotation.SuppressLint
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.telecom.CallAudioState
import android.telecom.InCallService
import com.chooloo.www.koler.KolerApp
import com.chooloo.www.koler.data.call.Call
import com.chooloo.www.koler.data.call.CallNotification
import com.chooloo.www.koler.ui.call.CallActivity

@SuppressLint("NewApi")
class CallService : InCallService() {
    private val _callNotification by lazy { CallNotification.getInstance(this) }
    private val componentRoot get() = (applicationContext as KolerApp).component


    override fun onCreate() {
        super.onCreate()
        sInstance = this
        _callNotification.attach()
    }

    override fun onDestroy() {
        super.onDestroy()
        _callNotification.detach()
    }

    override fun onCallAdded(telecomCall: android.telecom.Call) {
        super.onCallAdded(telecomCall)
        componentRoot.calls.entryAddCall(Call(telecomCall))
        if (!sIsActivityActive) {
            startCallActivity()
        }
    }

    override fun onCallRemoved(telecomCall: android.telecom.Call) {
        super.onCallRemoved(telecomCall)
        componentRoot.calls.getCallByTelecomCall(telecomCall)
            ?.let(componentRoot.calls::entryRemoveCall)
    }

    override fun onCallAudioStateChanged(audioState: CallAudioState) {
        super.onCallAudioStateChanged(audioState)
        componentRoot.callAudios.entryCallAudioStateChanged(callAudioState)
    }

    private fun startCallActivity() {
        val intent = Intent(this, CallActivity::class.java)
        intent.addFlags(FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }


    companion object {
        var sIsActivityActive = false
        var sInstance: CallService? = null
    }
}