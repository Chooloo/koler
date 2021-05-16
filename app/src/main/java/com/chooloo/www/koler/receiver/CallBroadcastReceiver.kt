package com.chooloo.www.koler.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.chooloo.www.koler.util.call.CallsManager

class CallBroadcastReceiver : BroadcastReceiver() {
    companion object {
        const val ACTION_ANSWER = "action_answer"
        const val ACTION_HANGUP = "action_hangup"
    }

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            ACTION_ANSWER -> CallsManager.firstCall?.answer()
            ACTION_HANGUP -> CallsManager.firstCall?.reject()
        }
    }
}