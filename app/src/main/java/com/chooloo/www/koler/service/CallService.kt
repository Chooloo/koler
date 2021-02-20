package com.chooloo.www.koler.service

import android.content.Intent
import android.telecom.Call
import android.telecom.InCallService
import com.chooloo.www.koler.ui.call.CallActivity
import com.chooloo.www.koler.util.call.CallManager

class CallService : InCallService() {
    override fun onCallAdded(call: Call) {
        super.onCallAdded(call)
        CallManager.sCall = call
        goToCallActivity()
    }

    override fun onCallRemoved(call: Call) {
        super.onCallRemoved(call)
        CallManager.sCall = null
    }

    private fun goToCallActivity() {
        startActivity(Intent(this, CallActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        })
    }
}