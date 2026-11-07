package com.chooloo.www.koler.service

import android.annotation.SuppressLint
import android.app.NotificationManager.IMPORTANCE_DEFAULT
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.chooloo.www.chooloolib.R as LibR
import com.chooloo.www.chooloolib.di.IoDispatcher
import com.chooloo.www.chooloolib.domain.repository.PhoneRepository
import com.chooloo.www.koler.R
import com.chooloo.www.koler.ui.main.ACTION_SHOW_MISSED_CALLS
import com.chooloo.www.koler.ui.main.MainActivity
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

@Singleton
class MissedCallNotification @Inject constructor(
    @ApplicationContext private val context: Context,
    private val phoneRepository: PhoneRepository,
    private val notificationManager: NotificationManagerCompat,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) {
    private val mutex = Mutex()

    suspend fun update(count: Int, number: String?) = mutex.withLock {
        withContext(ioDispatcher) {
            if (count > 0) show(count, number) else notificationManager.cancel(ID)
        }
    }

    // Blocked notifications are checked by areNotificationsEnabled, which lint does not recognize as a permission check.
    @SuppressLint("MissingPermission")
    private suspend fun show(count: Int, number: String?) {
        if (!notificationManager.areNotificationsEnabled()) return
        notificationManager.createNotificationChannel(channel())
        val (title, text) = if (count == 1) {
            context.getString(LibR.string.missed_call_notification_title) to callerName(number)
        } else {
            context.getString(LibR.string.missed_calls_notification_title) to
                context.resources.getQuantityString(LibR.plurals.missed_calls_notification_text, count, count)
        }
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_call_missed)
            .setCategory(NotificationCompat.CATEGORY_MISSED_CALL)
            .setContentTitle(title)
            .setContentText(text)
            .setContentIntent(openRecentsIntent())
            .setDeleteIntent(dismissIntent())
            .setAutoCancel(true)
            .build()
        notificationManager.notify(ID, notification)
    }

    private suspend fun callerName(number: String?): String =
        number?.takeIf { it.isNotBlank() }?.let { phoneRepository.lookup(it)?.name ?: it }
            ?: context.getString(LibR.string.caller_unknown)

    private fun openRecentsIntent() = PendingIntent.getActivity(
        context,
        0,
        Intent(context, MainActivity::class.java)
            .setAction(ACTION_SHOW_MISSED_CALLS)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP),
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    private fun dismissIntent() = PendingIntent.getBroadcast(
        context,
        0,
        Intent(context, MissedCallDismissReceiver::class.java),
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    // The phone already rang, and Telecom replays every unread call after a reboot, so this channel stays silent.
    private fun channel() = NotificationChannelCompat.Builder(CHANNEL_ID, IMPORTANCE_DEFAULT)
        .setName(context.getString(LibR.string.missed_call_notification_channel_name))
        .setDescription(context.getString(LibR.string.missed_call_notification_channel_description))
        .setSound(null, null)
        .build()

    private companion object {
        const val ID = 421
        const val CHANNEL_ID = "missed_calls"
    }
}
