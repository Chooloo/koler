package com.chooloo.www.callmanager.entity;

import android.database.Cursor;
import android.text.format.DateFormat;

import androidx.annotation.IntDef;
import androidx.annotation.Nullable;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Date;

import static android.provider.CallLog.Calls.INCOMING_TYPE;
import static android.provider.CallLog.Calls.MISSED_TYPE;
import static android.provider.CallLog.Calls.NUMBER;
import static android.provider.CallLog.Calls.OUTGOING_TYPE;
import static android.provider.CallLog.Calls.REJECTED_TYPE;
import static android.provider.CallLog.Calls.VOICEMAIL_TYPE;
import static com.chooloo.www.callmanager.cursorloader.RecentsCursorLoader.COLUMN_DATE;
import static com.chooloo.www.callmanager.cursorloader.RecentsCursorLoader.COLUMN_DURATION;
import static com.chooloo.www.callmanager.cursorloader.RecentsCursorLoader.COLUMN_ID;
import static com.chooloo.www.callmanager.cursorloader.RecentsCursorLoader.COLUMN_NUMBER;
import static com.chooloo.www.callmanager.cursorloader.RecentsCursorLoader.COLUMN_TYPE;

public class RecentCall {

    private long callId;
    private int count;
    private final int callType;
    private final Date callDate;
    private final String number;
    private final String callDuration;

    public static final int TYPE_OUTGOING = OUTGOING_TYPE;
    public static final int TYPE_INCOMING = INCOMING_TYPE;
    public static final int TYPE_MISSED = MISSED_TYPE;
    public static final int TYPE_VOICEMAIL = VOICEMAIL_TYPE;
    public static final int TYPE_REJECTED = REJECTED_TYPE;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({TYPE_OUTGOING, TYPE_INCOMING, TYPE_MISSED, TYPE_VOICEMAIL, TYPE_REJECTED})
    public @interface CallType {
    }

    public RecentCall(long id, String number, String duration, Date date, int type, @Nullable int count) {
        this.callId = id;
        this.number = number;
        this.callDuration = duration;
        this.callDate = date;
        this.callType = type;
        this.count = count == 0 ? 1 : count;
    }

    public static RecentCall fromCursor(Cursor cursor) {
        try {
            long id = cursor.getLong(cursor.getColumnIndex(COLUMN_ID));
            String number = cursor.getString(cursor.getColumnIndex(COLUMN_NUMBER));
            String duration = cursor.getString(cursor.getColumnIndex(COLUMN_DURATION));
            Date date = new Date(cursor.getLong(cursor.getColumnIndex(COLUMN_DATE)));
            int type = cursor.getInt(cursor.getColumnIndex(COLUMN_TYPE));
            int count = checkNextMutliple(cursor, number);
            return new RecentCall(id, number, duration, date, type, count);
        } catch (IndexOutOfBoundsException e) {
            return unknownCall();
        }
    }

    public static RecentCall unknownCall() {
        return new RecentCall(0, "Unknown", null, null, -1, 0);
    }

    public long getCallId() {
        return this.callId;
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
        return DateFormat.format("yy ", this.callDate).toString() +
                new java.text.DateFormatSymbols().getShortMonths()[Integer.parseInt(DateFormat.format("MM", this.callDate).toString()) - 1] +
                DateFormat.format(" dd, hh:mm", this.callDate).toString();
    }

    /**
     * Check how many calls from the same contact are there from the current entry
     *
     * @param cursor
     * @return Amount of the calls from the same contact in a row
     */
    public static int checkNextMutliple(Cursor cursor, String number) {
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
