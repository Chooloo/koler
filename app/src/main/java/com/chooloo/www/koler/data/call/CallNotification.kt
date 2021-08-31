package com.chooloo.www.koler.data.call

import android.app.Notification
import android.app.Notification.EXTRA_NOTIFICATION_ID
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_CANCEL_CURRENT
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.chooloo.www.koler.KolerApp
import com.chooloo.www.koler.R
import com.chooloo.www.koler.data.call.Call.State.DISCONNECTED
import com.chooloo.www.koler.data.call.Call.State.DISCONNECTING
import com.chooloo.www.koler.interactor.calls.CallsInteractor
import com.chooloo.www.koler.receiver.CallBroadcastReceiver
import com.chooloo.www.koler.ui.call.CallActivity
import com.chooloo.www.koler.util.SingletonHolder

@RequiresApi(Build.VERSION_CODES.O)
class CallNotification(
    private val context: Context
) : CallsInteractor.Listener {
    private val componentRoot by lazy { (context.applicationContext as KolerApp).componentRoot }

    override fun onNoCalls() {
        cancel()
    }

    override fun onCallChanged(call: Call) {
        show(call)
    }

    override fun onMainCallChanged(call: Call) {
    }

    private val _answerIntent by lazy {
        Intent(context, CallBroadcastReceiver::class.java).apply {
            action = CallBroadcastReceiver.ACTION_ANSWER
            putExtra(EXTRA_NOTIFICATION_ID, ID)
        }
    }

    private val _hangupIntent by lazy {
        Intent(context, CallBroadcastReceiver::class.java).apply {
            action = CallBroadcastReceiver.ACTION_HANGUP
            putExtra(EXTRA_NOTIFICATION_ID, ID)
        }
    }

    private val _answerPendingIntent by lazy {
        PendingIntent.getBroadcast(
            context,
            0,
            _answerIntent,
            FLAG_CANCEL_CURRENT
        )
    }

    private val _hangupPendingIntent by lazy {
        PendingIntent.getBroadcast(
            context,
            1,
            _hangupIntent,
            FLAG_CANCEL_CURRENT
        )
    }

    private val _contentPendingIntent by lazy {
        PendingIntent.getActivity(
            context,
            0,
            Intent(context, CallActivity::class.java).apply {
                flags = FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK
            },
            0
        )
    }

    private val _channel by lazy {
        NotificationChannel(
            CHANNEL_ID,
            componentRoot.stringInteractor.getString(R.string.call_notification_channel_name),
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description =
                componentRoot.stringInteractor.getString(R.string.call_notification_channel_description)
            lightColor = componentRoot.colorInteractor.getAttrColor(R.attr.colorSecondary)
            enableLights(true)
        }
    }


    private fun buildNotification(call: Call): Notification {
        val account = componentRoot.phoneAccountsInteractor.lookupAccount(call.number)
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setWhen(0)
            .setOngoing(true)
            .setColorized(true)
            .setPriority(PRIORITY)
            .setOnlyAlertOnce(true)
            .setContentTitle(account.displayString)
            .setSmallIcon(R.drawable.icon_full_144)
            .setContentIntent(_contentPendingIntent)
            .setColor(componentRoot.colorInteractor.getAttrColor(R.attr.colorSecondary))
            .setContentText(componentRoot.stringInteractor.getString(call.state.stringRes))
        if (call.isIncoming) {
            builder.addAction(
                R.drawable.ic_call_black_24dp,
                componentRoot.stringInteractor.getString(R.string.action_answer),
                _answerPendingIntent
            )
        }
        if (call.state !in arrayOf(DISCONNECTED, DISCONNECTING)) {
            builder.addAction(
                R.drawable.ic_call_end_black_24dp,
                componentRoot.stringInteractor.getString(R.string.action_hangup),
                _hangupPendingIntent
            )
        }
        return builder.build()
    }


    fun show(call: Call) {
        componentRoot.notificationManager.notify(ID, buildNotification(call))
    }

    fun cancel() {
        componentRoot.notificationManager.cancel(ID)
    }

    fun createNotificationChannel() {
        componentRoot.notificationManager.createNotificationChannel(_channel)
    }


    companion object : SingletonHolder<CallNotification, Context>(::CallNotification) {
        const val ID = 420
        const val CHANNEL_ID = "call_notification_channel"
        const val PRIORITY = NotificationCompat.PRIORITY_HIGH
    }
}