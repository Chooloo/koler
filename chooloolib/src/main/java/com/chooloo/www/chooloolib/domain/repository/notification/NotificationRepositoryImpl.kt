package com.chooloo.www.chooloolib.domain.repository.notification

import android.Manifest.permission.POST_NOTIFICATIONS
import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.chooloo.www.chooloolib.domain.repository.base.BaseRepositoryImpl
import com.chooloo.www.chooloolib.domain.repository.notification.NotificationRepository.Companion.Priority
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
class NotificationRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val notificationManager: NotificationManagerCompat
) : BaseRepositoryImpl(), NotificationRepository {
    override fun createNotification(
        icon: Int?,
        color: Int?,
        isOngoing: Boolean,
        priority: Priority,
        category: String?,
        contentText: String?,
        contentTitle: String?,
        contentIntent: PendingIntent?,
        actions: List<NotificationCompat.Action>
    ): Notification {
        val builder = NotificationCompat.Builder(context, "")
        builder
            .setWhen(0)
            .setColorized(true)
            .setOngoing(isOngoing)
            .setCategory(category)
            .setOnlyAlertOnce(true)
            .setContentText(contentText)
            .setContentTitle(contentTitle)
            .setPriority(priority.priority)
            .setContentIntent(contentIntent)
        color?.let(builder::setColor)
        icon?.let(builder::setSmallIcon)
        actions.forEach(builder::addAction)

        return builder.build()
    }

    override fun cancelNotification(id: Int) {
        notificationManager.cancel(id)
    }

    @RequiresPermission(POST_NOTIFICATIONS)
    override fun showNotification(id: Int, notification: Notification) {
        notificationManager.notify(id, notification)
    }

    override fun createNotificationChannel(
        id: String,
        name: String,
        importance: Int,
        description: String
    ) = NotificationChannelCompat.Builder(id, importance)
        .setName(name)
        .setDescription(description)
        .build()
        .also(notificationManager::createNotificationChannel)
}