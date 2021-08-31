package com.chooloo.www.koler.service

import android.annotation.SuppressLint
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.telecom.CallAudioState.ROUTE_SPEAKER
import android.telecom.CallAudioState.ROUTE_WIRED_OR_EARPIECE
import android.telecom.InCallService
import com.chooloo.www.koler.KolerApp
import com.chooloo.www.koler.data.call.Call
import com.chooloo.www.koler.data.call.CallNotification
import com.chooloo.www.koler.ui.call.CallActivity

@SuppressLint("NewApi")
class CallService : InCallService() {
    private lateinit var _callNotification: CallNotification
    private val componentRoot get() = (applicationContext as KolerApp).componentRoot

    override fun onCreate() {
        super.onCreate()
        sInstance = this
        _callNotification = CallNotification(applicationContext)
        componentRoot.callsInteractor.registerListener(_callNotification)
    }

    override fun onDestroy() {
        super.onDestroy()
        componentRoot.callsInteractor.unregisterListener(_callNotification)
    }

    override fun onCallAdded(telecomCall: android.telecom.Call) {
        super.onCallAdded(telecomCall)
        // new base call is added, this is the first place the code knows about the call
        // add it to the call interactor (call manager)
        componentRoot.callsInteractor.entryAddCall(Call(telecomCall))
        if (!sIsActivityActive) {
            startCallActivity()
        }
    }

    override fun onCallRemoved(telecomCall: android.telecom.Call) {
        super.onCallRemoved(telecomCall)
        // a base call was removed from service,
        // this is the first place the code knows a call was removed
        // remove it from the call interactor (call manager)
        componentRoot.callsInteractor.getCallByTelecomCall(telecomCall)?.let {
            componentRoot.callsInteractor.entryRemoveCall(it)
        }
    }


    private fun startCallActivity() {
        val intent = Intent(this, CallActivity::class.java)
        intent.addFlags(FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }


    companion object {
        private lateinit var sInstance: CallService

        var sIsActivityActive = false

        fun toggleSpeakerRoute(isSpeakerOn: Boolean) {
            sInstance.setAudioRoute(if (isSpeakerOn) ROUTE_SPEAKER else ROUTE_WIRED_OR_EARPIECE)
        }
    }
}