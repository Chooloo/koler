package com.chooloo.www.koler.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telecom.TelecomManager
import com.chooloo.www.chooloolib.di.ApplicationScope
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MissedCallReceiver : BroadcastReceiver() {
    @Inject lateinit var missedCallNotification: MissedCallNotification
    @Inject @ApplicationScope lateinit var applicationScope: CoroutineScope

    override fun onReceive(context: Context, intent: Intent) {
        // The receiver is exported, so an explicit intent from another app can reach it with any action.
        if (intent.action != TelecomManager.ACTION_SHOW_MISSED_CALLS_NOTIFICATION) return
        val count = intent.getIntExtra(TelecomManager.EXTRA_NOTIFICATION_COUNT, 0)
        val number = intent.getStringExtra(TelecomManager.EXTRA_NOTIFICATION_PHONE_NUMBER)
        val pendingResult = goAsync()
        // UNDISPATCHED queues on the notification's lock before returning, so broadcasts apply in arrival order.
        applicationScope.launch(start = CoroutineStart.UNDISPATCHED) {
            try {
                missedCallNotification.update(count, number)
            } finally {
                pendingResult.finish()
            }
        }
    }
}
