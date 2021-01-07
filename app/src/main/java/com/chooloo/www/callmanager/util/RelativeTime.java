package com.chooloo.www.callmanager.util;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Return a string of a given time according to it's difference from the current time
 */
public class RelativeTime {
    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;

    public static Date currentDate() {
        return Calendar.getInstance().getTime();
    }

    public static String getTimeAgo(long time) {
        final long now = currentDate().getTime(); // get current time
        final long diff = now - time; // get the time difference between now and the given time
        if (diff < 0) {
            return  "In the future"; // if time is in the future
        }

        // return a string according to time difference from now
        if (diff < MINUTE_MILLIS) {
            return "Moments ago";
        } else if (diff < 2 * MINUTE_MILLIS) {
            return "A minute ago";
        } else if (diff < HOUR_MILLIS) {
            return diff / MINUTE_MILLIS + " minutes ago";
        } else if (diff < 2 * HOUR_MILLIS) {
            return "An hour ago";
        } else if (diff < DAY_MILLIS) {
            return diff / HOUR_MILLIS + " hours ago";
        } else if (diff < 2 * DAY_MILLIS) {
            return "Yesterday";
        } else {
            android.text.format.DateFormat dateFormat = new android.text.format.DateFormat();
            return new java.text.DateFormatSymbols().getShortMonths()[Integer.parseInt(dateFormat.format("MM", time).toString()) - 1] +
                    dateFormat.format(" dd, hh:mm", time).toString();
        }
    }
}
