package com.chooloo.www.koler.util

import android.content.Context
import android.text.format.DateFormat
import android.text.format.DateUtils
import java.text.DateFormatSymbols
import java.text.SimpleDateFormat
import java.util.*

private const val SECOND_MILLIS = 1000
private const val MINUTE_MILLIS = 60 * SECOND_MILLIS
private const val HOUR_MILLIS = 60 * MINUTE_MILLIS
private const val DAY_MILLIS = 24 * HOUR_MILLIS

val currentDate: Date
    get() = Calendar.getInstance().time

fun getElapsedTimeString(seconds: Long): String {
    val seconds = seconds % 60
    val minutes = (seconds % 3600) / 60
    val hours = seconds / 3600
    return "${if (hours != 0L) "$hours hrs " else ""}$minutes mins $seconds sec"
}

fun Context.getHoursString(date: Date) =
    SimpleDateFormat(if (DateFormat.is24HourFormat(this)) "HH:mm" else "hh:mm aa").format(date)
        .toString()

fun getRelativeDateString(date: Date?): String {
    val now = currentDate.time
    val date = date?.time ?: 0
    return if (now < date && (date - now) < 864000) {
        "Tomorrow"
    } else if (now > date && (now - date) < 864000) {
        "Yesterday"
    } else {
        DateUtils.getRelativeTimeSpanString(date, now, DateUtils.DAY_IN_MILLIS).toString()
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