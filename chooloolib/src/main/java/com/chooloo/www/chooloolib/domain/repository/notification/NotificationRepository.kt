package com.chooloo.www.chooloolib.domain.repository.notification

import android.app.Notification
import android.app.PendingIntent
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.PRIORITY_DEFAULT
import androidx.core.app.NotificationCompat.PRIORITY_HIGH
import androidx.core.app.NotificationCompat.PRIORITY_LOW
import androidx.core.app.NotificationCompat.PRIORITY_MAX
import androidx.core.app.NotificationCompat.PRIORITY_MIN
import com.chooloo.www.chooloolib.domain.repository.base.BaseRepository

interface NotificationRepository : BaseRepository {
    companion object {
        enum class Priority(val priority: Int) {
            MAX(PRIORITY_MAX),
            MIN(PRIORITY_MIN),
            LOW(PRIORITY_LOW),
            HIGH(PRIORITY_HIGH),
            DEFAULT(PRIORITY_DEFAULT)
        }
    }

    fun cancelNotification(id: Int)

    fun showNotification(id: Int, notification: Notification)

    fun createNotification(
        icon: Int? = null,
        color: Int? = null,
        isOngoing: Boolean,
        priority: Priority,
        category: String? = null,
        contentText: String? = null,
        contentTitle: String? = null,
        contentIntent: PendingIntent? = null,
        actions: List<NotificationCompat.Action> = listOf()
    ): Notification

    fun createNotificationChannel(
        id: String,
        name: String,
        importance: Int,
        description: String,
    ): NotificationChannelCompat
}