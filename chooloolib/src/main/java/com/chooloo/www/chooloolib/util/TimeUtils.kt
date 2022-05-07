package com.chooloo.www.chooloolib.util

import android.content.Context
import android.text.format.DateFormat
import android.text.format.DateUtils
import androidx.annotation.StringRes
import com.chooloo.www.chooloolib.BaseApp
import com.chooloo.www.chooloolib.R
import java.text.DateFormatSymbols
import java.text.SimpleDateFormat
import java.util.*

private const val SECOND_MILLIS = 1000
private const val MINUTE_MILLIS = 60 * SECOND_MILLIS
private const val HOUR_MILLIS = 60 * MINUTE_MILLIS
private const val DAY_MILLIS = 24 * HOUR_MILLIS

object Strings {
    fun get(@StringRes stringRes: Int, vararg formatArgs: Any = emptyArray()): String {
        return BaseApp.instance.getString(stringRes, *formatArgs)
    }
}

val currentDate: Date
    get() = Calendar.getInstance().time

fun getElapsedTimeString(total_seconds: Long): String {
    val seconds = total_seconds % 60
    val minutes = (total_seconds / 60).toInt()
    val hours = (total_seconds / 3600).toInt()
    return if (hours != 0) hours.toString() + " " + Strings.get(R.string.time_hours_abbrev) + " "
    else minutes.toString() + " " + Strings.get(R.string.time_minutes_abbrev) + " " +
    seconds.toString() + " " + Strings.get(R.string.time_seconds_abbrev)
}

fun Context.getHoursString(date: Date) =
    SimpleDateFormat(if (DateFormat.is24HourFormat(this)) "HH:mm" else "hh:mm aa", Locale.US)
        .format(date)
        .toString()

fun getRelativeDateString(date: Date?): String {
    val now = currentDate.time
    val time = date?.time ?: currentDate.time
    return when {
        DateUtils.isToday(time - DateUtils.DAY_IN_MILLIS) -> Strings.get(R.string.time_tomorrow)
        DateUtils.isToday(time + DateUtils.DAY_IN_MILLIS) -> Strings.get(R.string.time_yesterday)
        else -> DateUtils.getRelativeTimeSpanString(time, now, DateUtils.DAY_IN_MILLIS).toString()
    }
}

fun getTimeAgo(time: Long): String {
    val now = currentDate.time // get current time
    val diff = now - time // get the time difference between now and the given time

    if (diff < 0) {
        return Strings.get(R.string.time_in_the_future) // if time is in the future
    }

    // return a string according to time difference from now
    return when {
        diff < MINUTE_MILLIS -> Strings.get(R.string.time_moments_ago)
        diff < 2 * MINUTE_MILLIS -> Strings.get(R.string.time_a_minute_ago)
        diff < HOUR_MILLIS -> Strings.get(R.string.time_minutes_ago, diff / MINUTE_MILLIS)
        diff < 2 * HOUR_MILLIS -> Strings.get(R.string.time_an_hour_ago)
        diff < DAY_MILLIS -> Strings.get(R.string.time_hours_ago, diff / HOUR_MILLIS)
        diff < 2 * DAY_MILLIS -> Strings.get(R.string.time_yesterday)
        else -> {
            DateFormatSymbols().shortMonths[DateFormat.format("MM", time).toString()
                .toInt() - 1].toString() +
                    DateFormat.format(" dd, hh:mm", time).toString()
        }
    }
}