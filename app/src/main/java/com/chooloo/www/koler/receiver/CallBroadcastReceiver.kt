package com.chooloo.www.koler.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.chooloo.www.koler.KolerApp

class CallBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val callsInteractor = (context.applicationContext as KolerApp).componentRoot.callsInteractor
        when (intent.action) {
            ACTION_ANSWER -> callsInteractor.mainCall?.answer()
            ACTION_HANGUP -> callsInteractor.mainCall?.reject()
        }
    }

    companion object {
        const val ACTION_ANSWER = "action_answer"
        const val ACTION_HANGUP = "action_hangup"
    }
}