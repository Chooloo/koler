package com.chooloo.www.koler.service

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.telecom.Call
import android.telecom.DisconnectCause
import android.telecom.InCallService
import com.chooloo.www.koler.data.CallDetails
import com.chooloo.www.koler.ui.call.CallActivity
import com.chooloo.www.koler.ui.notification.CallNotification
import com.chooloo.www.koler.util.call.CallManager

@SuppressLint("NewApi")
class CallService : InCallService() {
    private val _callNotification by lazy { CallNotification(this) }
    private val _callNotificationCallback by lazy {
        object : CallManager.CallListener(this) {
            override fun onCallDetailsChanged(callDetails: CallDetails) {
                CallNotification(context).show(callDetails)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cancelCallNotification()
    }

    override fun onCallAdded(call: Call) {
        super.onCallAdded(call)
        CallManager.sCall = call

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            _callNotification.show(CallDetails.fromCall(call, this))
            CallManager.registerListener(_callNotificationCallback)
        }

        startActivity(Intent(this, CallActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        })
    }

    override fun onCallRemoved(call: Call) {
        super.onCallRemoved(call)
        CallManager.sCall = null
        cancelCallNotification()
        call.details?.let {
            if (call.details.disconnectCause.code == DisconnectCause.MISSED) {
                CallNotification(applicationContext).show(
                    CallDetails.fromCall(call, applicationContext)
                )
            }
        }
    }

    private fun cancelCallNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            _callNotification.cancel()
            CallManager.unregisterCallback(_callNotificationCallback)
        }
    }

}