package com.chooloo.www.koler.service

import android.content.Intent
import android.telecom.Call
import android.telecom.InCallService
import com.chooloo.www.koler.ui.call.CallActivity

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
        val intent = Intent(this, CallActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }
}