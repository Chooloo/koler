package com.chooloo.www.callmanager.database.entity;

import android.content.Context;
import android.database.Cursor;
import android.provider.CallLog;

import com.chooloo.www.callmanager.util.ContactUtils;

import java.text.DateFormat;
import java.util.Date;

public class RecentCall {

    // Attributes
    private Context mContext;
    private long mCallId;
    private String mCallerName;
    private String mNumber;
    private int mCallType;
    private String mCallDuration;
    private Date mCallDate;
    private int mCount;

    // Call Types
    public static final int mOutgoingCall = CallLog.Calls.OUTGOING_TYPE;
    public static final int mIncomingCall = CallLog.Calls.INCOMING_TYPE;
    public static final int mMissedCall = CallLog.Calls.MISSED_TYPE;

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
        this.mNumber = number;
        this.mCallerName = ContactUtils.getContactByPhoneNumber(this.mContext, number).getName();
        this.mCallType = type;
        this.mCallDuration = duration;
        this.mCallDate = date;
    }

    public RecentCall(Context context, Cursor cursor) {
        this.mContext = context;
        this.mCallId = cursor.getLong(cursor.getColumnIndex(CallLog.Calls._ID));
        mNumber = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));
        String callerName = cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME));
        if ((callerName == null || callerName.isEmpty()) && mNumber != null) {
            Contact contact = ContactUtils.getContactByPhoneNumber(context, mNumber);
            if (contact != null) mCallerName = contact.getName();
        } else {
            mCallerName = callerName;
        }
        mCallDuration = cursor.getString(cursor.getColumnIndex(CallLog.Calls.DURATION));
        mCallDate = new Date(cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DATE)));
        mCallType = cursor.getInt(cursor.getColumnIndex(CallLog.Calls.TYPE));
        int position = cursor.getPosition();
        mCount = checkNextMutliple(cursor);
        cursor.moveToPosition(position);
    }

    public long getCallId() {
        return this.mCallId;
    }

    public String getCallerName() {
        return this.mCallerName;
    }

    public String getCallerNumber() {
        return this.mNumber;
    }

    public int getCallType() {
        return this.mCallType;
    }

    public String getCallDuration() {
        return this.mCallDuration;
    }

    public Date getCallDate() {
        return this.mCallDate;
    }

    public String getCallDateString() {
        android.text.format.DateFormat dateFormat = new android.text.format.DateFormat();
        return dateFormat.format("yy ", this.mCallDate).toString() +
                new java.text.DateFormatSymbols().getShortMonths()[Integer.parseInt(dateFormat.format("MM", this.mCallDate).toString()) - 1] +
                dateFormat.format(" dd, hh:mm", this.mCallDate).toString();
    }

    public int getCount() {
        return this.mCount;
    }

    public int checkNextMutliple(Cursor cursor) {
        int count = 1;
        while (true) {
            try {
                cursor.moveToNext();
                if (cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER)).equals(mNumber))
                    count++;
                else return count;
            } catch (Exception e) {
                // probably index out of bounds exception
                return count;
            }
        }
    }
}
