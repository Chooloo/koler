package com.chooloo.www.callmanager.database.entity;

import android.content.Context;
import android.database.Cursor;

import com.chooloo.www.callmanager.util.ContactUtils;

import java.util.Date;

import timber.log.Timber;

import static android.provider.CallLog.Calls.*;
import static com.chooloo.www.callmanager.cursorloader.RecentsCursorLoader.COLUMN_DATE;
import static com.chooloo.www.callmanager.cursorloader.RecentsCursorLoader.COLUMN_DURATION;
import static com.chooloo.www.callmanager.cursorloader.RecentsCursorLoader.COLUMN_ID;
import static com.chooloo.www.callmanager.cursorloader.RecentsCursorLoader.COLUMN_NUMBER;
import static com.chooloo.www.callmanager.cursorloader.RecentsCursorLoader.COLUMN_TYPE;

public class RecentCall {

    // Attributes
    private Context mContext;
    private long callId;
    private String callerName;
    private String number;
    private int callType;
    private String callDuration;
    private Date callDate;
    private int count;

    // Call Types
    public static final int TYPE_OUTGOING = OUTGOING_TYPE;
    public static final int TYPE_INCOMING = INCOMING_TYPE;
    public static final int TYPE_MISSED = MISSED_TYPE;
    public static final int TYPE_VOICEMAIL = VOICEMAIL_TYPE;
    public static final int TYPE_REJECTED = REJECTED_TYPE;

    /**
     * Constructor
     *
     * @param number   caller's number
     * @param type     call's type (out/in/missed)
     * @param duration call's duration
     * @param date     call's date
     */
    public RecentCall(Context context, String number, int type, String duration, Date date) {
        this.mContext = context;
        this.number = number;
        this.callerName = ContactUtils.lookupContact(context, number).getName();
        this.callType = type;
        this.callDuration = duration;
        this.callDate = date;
    }

    public RecentCall(Context context, Cursor cursor) {
        this.mContext = context;
        this.callId = cursor.getLong(cursor.getColumnIndex(COLUMN_ID));
        this.number = cursor.getString(cursor.getColumnIndex(COLUMN_NUMBER));
        Timber.i("Recent Call Number: " + this.number);
        this.callerName = ContactUtils.lookupContact(context, this.number).getName();
        Timber.i("Recent Call Name: " + this.callerName);
        this.callDuration = cursor.getString(cursor.getColumnIndex(COLUMN_DURATION));
        this.callDate = new Date(cursor.getLong(cursor.getColumnIndex(COLUMN_DATE)));
        this.callType = cursor.getInt(cursor.getColumnIndex(COLUMN_TYPE));
        this.count = checkNextMutliple(cursor);
        cursor.moveToPosition(cursor.getPosition());
    }

    public long getCallId() {
        return this.callId;
    }

    public String getCallerName() {
        return this.callerName;
    }

    public String getCallerNumber() {
        return this.number;
    }

    public int getCallType() {
        return this.callType;
    }

    public String getCallDuration() {
        return this.callDuration;
    }

    public Date getCallDate() {
        return this.callDate;
    }

    public int getCount() {
        return this.count;
    }

    /**
     * Return a string representing the date of the call relatively to the current time
     *
     * @return String
     */
    public String getCallDateString() {
        android.text.format.DateFormat dateFormat = new android.text.format.DateFormat();
        return dateFormat.format("yy ", this.callDate).toString() +
                new java.text.DateFormatSymbols().getShortMonths()[Integer.parseInt(dateFormat.format("MM", this.callDate).toString()) - 1] +
                dateFormat.format(" dd, hh:mm", this.callDate).toString();
    }

    /**
     * Check how many calls from the same contact are there from the current entry
     *
     * @param cursor
     * @return Amount of the calls from the same contact in a row
     */
    public int checkNextMutliple(Cursor cursor) {
        int count = 1;
        while (true) {
            try {
                cursor.moveToNext();
                if (cursor.getString(cursor.getColumnIndex(NUMBER)).equals(number)) count++;
                else return count;
            } catch (Exception e) { // probably index out of bounds exception
                return count;
            }
        }
    }
}
