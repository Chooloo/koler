package com.chooloo.www.koler.testcall

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.telecom.TelecomManager

class TestCallReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            ACTION_INCOMING -> addIncoming(context, number(intent))
            ACTION_UPDATE -> TestConnectionService.updateRinging(number(intent))
            ACTION_HANGUP -> TestConnectionService.hangupAll()
            else -> error("Unknown test call action ${intent.action}")
        }
        resultCode = Activity.RESULT_OK
    }

    private fun addIncoming(context: Context, number: String) {
        val extras = Bundle().apply {
            putString(TelecomManager.EXTRA_INCOMING_CALL_ADDRESS, number)
        }
        context.getSystemService(TelecomManager::class.java)
            .addNewIncomingCall(TestConnectionService.accountHandle(context), extras)
    }

    private fun number(intent: Intent): String =
        intent.getStringExtra(EXTRA_NUMBER) ?: error("Missing --es $EXTRA_NUMBER")

    private companion object {
        const val ACTION_INCOMING = "com.chooloo.www.koler.test.INCOMING"
        const val ACTION_UPDATE = "com.chooloo.www.koler.test.UPDATE"
        const val ACTION_HANGUP = "com.chooloo.www.koler.test.HANGUP"
        const val EXTRA_NUMBER = "number"
    }
}
