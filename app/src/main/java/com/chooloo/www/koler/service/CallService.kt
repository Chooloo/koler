package com.chooloo.www.koler.service

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.telecom.Call
import android.telecom.CallAudioState
import android.telecom.DisconnectCause
import android.telecom.InCallService
import com.chooloo.www.koler.ui.call.CallActivity
import com.chooloo.www.koler.ui.notification.CallNotification
import com.chooloo.www.koler.util.call.CallItem
import com.chooloo.www.koler.util.call.CallsManager

@SuppressLint("NewApi")
class CallService : InCallService() {
    companion object {
        private lateinit var sInstance: CallService

        fun toggleSpeakerRoute(isSpeakerOn: Boolean) {
            sInstance.setAudioRoute(if (isSpeakerOn) CallAudioState.ROUTE_SPEAKER else CallAudioState.ROUTE_WIRED_OR_EARPIECE)
        }
    }

    private var _firstCall: Call? = null
    private val _callNotification by lazy { CallNotification(this) }
    private val _callNotificationCallback by lazy {
        object : CallsManager.CallsListener {
            override fun onCallDetailsChanged(callItem: CallItem) {
                CallNotification(this@CallService).show(callItem)
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        sInstance = this
    }

    override fun onDestroy() {
        super.onDestroy()
        cancelCallNotification()
    }

    override fun onCallAdded(call: Call) {
        super.onCallAdded(call)
        _firstCall = _firstCall ?: call
        CallsManager.addCall(call)

        if (CallsManager.sCalls.size == 1) {
            startActivity(Intent(this, CallActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            })
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                _callNotification.show(CallItem(call))
                CallsManager.registerListener(_callNotificationCallback)
            }
        }
    }

    override fun onCallRemoved(call: Call) {
        super.onCallRemoved(call)
        CallsManager.removeCall(call)

        if (call == _firstCall) {
            cancelCallNotification()
            call.details?.let {
                if (call.details.disconnectCause.code == DisconnectCause.MISSED) {
                    CallNotification(applicationContext).show(CallItem(call))
                }
            }
        }
    }

    private fun cancelCallNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            _callNotification.cancel()
            CallsManager.unregisterListener(_callNotificationCallback)
        }
    }
}