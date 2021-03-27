package com.chooloo.www.koler.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.chooloo.www.koler.util.call.CallManager

class CallBroadcastReceiver : BroadcastReceiver() {
    companion object {
        const val ACTION_ANSWER = "action_answer"
        const val ACTION_HANGUP = "action_hangup"
        const val ACTION_HOLD = "action_hold"
        const val ACTION_UNHOLD = "action_unhold"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent?.action) {
            ACTION_ANSWER -> CallManager.answer()
            ACTION_HANGUP -> CallManager.reject()
            ACTION_HOLD -> CallManager.hold(true)
            ACTION_UNHOLD -> CallManager.hold(false)
        }
    }
}