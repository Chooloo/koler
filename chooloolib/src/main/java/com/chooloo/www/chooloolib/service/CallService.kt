package com.chooloo.www.chooloolib.service

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.content.pm.PackageManager
import android.telecom.CallAudioState
import android.telecom.InCallService
import com.chooloo.www.chooloolib.BaseApp
import com.chooloo.www.chooloolib.data.call.Call
import com.chooloo.www.chooloolib.data.call.CallNotification

@SuppressLint("NewApi")
class CallService : InCallService() {
    private val _callNotification by lazy { CallNotification.getInstance(this) }
    private val component get() = (applicationContext as BaseApp).component


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
        component.calls.entryAddCall(Call(telecomCall))
        if (!sIsActivityActive) {
            startCallActivity()
        }
    }

    override fun onCallRemoved(telecomCall: android.telecom.Call) {
        super.onCallRemoved(telecomCall)
        component.calls.getCallByTelecomCall(telecomCall)
            ?.let(component.calls::entryRemoveCall)
    }

    override fun onCallAudioStateChanged(audioState: CallAudioState) {
        super.onCallAudioStateChanged(audioState)
        component.callAudios.entryCallAudioStateChanged(callAudioState)
    }

    private fun startCallActivity() {
        val appInfo = packageManager.getServiceInfo(
            ComponentName(this, this.javaClass),
            PackageManager.GET_META_DATA
        )
        appInfo.metaData.getString("call_activity")?.let {
            val intent = Intent(this, Class.forName(it))
            intent.addFlags(FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }


    companion object {
        var sIsActivityActive = false
        var sInstance: CallService? = null
    }
}