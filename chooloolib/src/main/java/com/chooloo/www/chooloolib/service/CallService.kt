package com.chooloo.www.chooloolib.service

import android.annotation.SuppressLint
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.telecom.CallAudioState
import android.telecom.InCallService
import com.chooloo.www.chooloolib.data.call.Call
import com.chooloo.www.chooloolib.interactor.callaudio.CallAudiosInteractor
import com.chooloo.www.chooloolib.interactor.calls.CallsInteractor
import com.chooloo.www.chooloolib.notification.CallNotification
import com.chooloo.www.chooloolib.ui.call.CallActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@SuppressLint("NewApi")
@AndroidEntryPoint
class CallService : InCallService() {
    @Inject lateinit var callsInteractor: CallsInteractor
    @Inject lateinit var callNotification: CallNotification
    @Inject lateinit var callAudiosInteractor: CallAudiosInteractor

    
    override fun onCreate() {
        super.onCreate()
        sInstance = this
        callNotification.attach()
    }

    override fun onDestroy() {
        super.onDestroy()
        callNotification.detach()
    }

    override fun onCallAdded(telecomCall: android.telecom.Call) {
        super.onCallAdded(telecomCall)
        callsInteractor.entryAddCall(Call(telecomCall))
        if (!sIsActivityActive) {
            startCallActivity()
        }
    }

    override fun onCallRemoved(telecomCall: android.telecom.Call) {
        super.onCallRemoved(telecomCall)
        callsInteractor.getCallByTelecomCall(telecomCall)
            ?.let(callsInteractor::entryRemoveCall)
    }

    override fun onCallAudioStateChanged(audioState: CallAudioState) {
        super.onCallAudioStateChanged(audioState)
        callAudiosInteractor.entryCallAudioStateChanged(callAudioState)
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