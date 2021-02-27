package com.chooloo.www.koler.util

import android.text.format.DateFormat
import java.text.DateFormatSymbols
import java.text.SimpleDateFormat
import java.util.*

/**
 * Return a string of a given time according to it's difference from the current time
 */
object RelativeTime {
    private const val SECOND_MILLIS = 1000
    private const val MINUTE_MILLIS = 60 * SECOND_MILLIS
    private const val HOUR_MILLIS = 60 * MINUTE_MILLIS
    private const val DAY_MILLIS = 24 * HOUR_MILLIS

    private val currentDate: Date
        get() = Calendar.getInstance().time

    fun getDateString(date: Date?) =
        date?.let { SimpleDateFormat("MMMM dd, yyyy").format(date).toString() } ?: ""

    fun getHoursString(date: Date?) =
        date?.let { SimpleDateFormat("HH:mm").format(date).toString() } ?: ""

    fun getRelativeDateString(date: Date?): String {
        if (date == null) return "Some time ago"

        val now = currentDate.time
        val diff = now - date.time
        if (diff < 0) {
            return "In the future"
        }
        return when {
            diff < DAY_MILLIS -> "Today"
            diff < 2 * DAY_MILLIS -> "Yesterday"
            else -> getDateString(date)
        }
    }

    fun getTimeAgo(time: Long): String {
        val now = currentDate.time // get current time
        val diff = now - time; // get the time difference between now and the given time

        if (diff < 0) {
            return "In the future"; // if time is in the future
        }

        // return a string according to time difference from now
        return when {
            diff < MINUTE_MILLIS -> "Moments ago"
            diff < 2 * MINUTE_MILLIS -> "A minute ago"
            diff < HOUR_MILLIS -> "${diff / MINUTE_MILLIS} minutes ago"
            diff < 2 * HOUR_MILLIS -> "An hour ago"
            diff < DAY_MILLIS -> "${diff / HOUR_MILLIS} hours ago"
            diff < 2 * DAY_MILLIS -> "Yesterday"
            else -> {
                DateFormatSymbols().shortMonths[DateFormat.format("MM", time).toString()
                    .toInt() - 1].toString() +
                        DateFormat.format(" dd, hh:mm", time).toString()
            }
        }
    }
}