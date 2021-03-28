package com.chooloo.www.koler.ui.notification

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
import com.chooloo.www.koler.R
import com.chooloo.www.koler.data.CallDetails
import com.chooloo.www.koler.data.CallDetails.CallState.*
import com.chooloo.www.koler.receiver.CallBroadcastReceiver
import com.chooloo.www.koler.ui.call.CallActivity
import com.chooloo.www.koler.util.getAttrColor

@RequiresApi(Build.VERSION_CODES.O)
class CallNotification(private val context: Context) {
    companion object {
        const val ID = 420
        const val CHANNEL_ID = "call_notification_channel"
        const val PRIORITY = NotificationCompat.PRIORITY_HIGH
    }

    private val sAnswer by lazy { context.getString(R.string.action_answer) }
    private val sHangup by lazy { context.getString(R.string.action_hangup) }
    private val sChannelName by lazy { context.getString(R.string.call_notification_channel_name) }
    private val sChannelDescription by lazy { context.getString(R.string.call_notification_channel_description) }
    private val _notificationManager by lazy { context.getSystemService(NotificationManager::class.java) as NotificationManager }

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
            sChannelName,
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = sChannelDescription
            lightColor = context.getAttrColor(R.attr.colorSecondary)

            enableLights(true)
        }
    }

    private fun buildNotification(callDetails: CallDetails): Notification {
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setWhen(0)
            .setPriority(PRIORITY)
            .setContentText(
                context.getString(
                    when (callDetails.callState) {
                        ACTIVE -> R.string.call_status_active
                        DISCONNECTED -> R.string.call_status_disconnected
                        RINGING -> R.string.call_status_incoming
                        DIALING -> R.string.call_status_dialing
                        CONNECTING -> R.string.call_status_dialing
                        HOLDING -> R.string.call_status_holding
                        else -> R.string.call_status_active
                    }
                )
            )
            .setSmallIcon(R.drawable.icon_full_144)
            .setContentIntent(_contentPendingIntent)
            .setColor(context.getAttrColor(R.attr.colorSecondary))
            .setOngoing(true)
            .setColorized(true)
            .setContentTitle(callDetails.contact.name ?: callDetails.contact.number)
        if (callDetails.callState == RINGING) {
            builder.addAction(R.drawable.ic_call_black_24dp, sAnswer, _answerPendingIntent)
        }
        if (callDetails.callState !in arrayOf(DISCONNECTED, DISCONNECTING)) {
            builder.addAction(R.drawable.ic_call_end_black_24dp, sHangup, _hangupPendingIntent)
        }
        return builder.build()
    }

    fun show(callDetails: CallDetails) {
        _notificationManager.notify(ID, buildNotification(callDetails))
    }

    fun cancel() {
        _notificationManager.cancel(ID)
    }

    fun createNotificationChannel() {
        _notificationManager.createNotificationChannel(_channel)
    }
}