package com.chooloo.www.koler.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.chooloo.www.koler.ui.call.OngoingCallActivity
import com.chooloo.www.koler.util.call.CallManager

class NotificationActionReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            OngoingCallActivity.ACTION_ANSWER -> CallManager.answer()
            OngoingCallActivity.ACTION_HANGUP -> CallManager.reject()
        }
    }
}