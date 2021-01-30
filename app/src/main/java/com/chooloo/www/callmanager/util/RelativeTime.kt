package com.chooloo.www.callmanager.util

import android.text.format.DateFormat
import java.text.DateFormatSymbols
import java.util.*

/**
 * Return a string of a given time according to it's difference from the current time
 */
object RelativeTime {
    private const val SECOND_MILLIS = 1000
    private const val MINUTE_MILLIS = 60 * SECOND_MILLIS
    private const val HOUR_MILLIS = 60 * MINUTE_MILLIS
    private const val DAY_MILLIS = 24 * HOUR_MILLIS

    val currentDate: Date
        get() = Calendar.getInstance().time

    @JvmStatic
    fun getTimeAgo(time: Long): String {
        val timeMillis = if (time < 1000000000000L) time * 1000 else time
        val now = currentDate.time // get current time

        if (timeMillis > now || timeMillis <= 0) {
            return "in the future"
        }

        val diff = now - timeMillis // get the time difference between now and the given time
        return when {
            diff < MINUTE_MILLIS -> "Moments ago"
            diff < 2 * MINUTE_MILLIS -> "A minute ago"
            diff < 50 * MINUTE_MILLIS -> "${diff.div(MINUTE_MILLIS)} minutes ago"
            diff < 90 * MINUTE_MILLIS -> "An hour ago"
            diff < 24 * HOUR_MILLIS -> "${diff.div(HOUR_MILLIS)} hours ago"
            diff < 48 * HOUR_MILLIS -> "Yesterday"
            else -> DateFormatSymbols().shortMonths[DateFormat.format("MM", timeMillis).toString().toInt() - 1].toString() +
                    DateFormat.format(" dd, hh:mm", timeMillis).toString()
        }
    }
}