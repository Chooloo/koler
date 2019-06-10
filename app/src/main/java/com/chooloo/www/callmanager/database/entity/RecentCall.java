package com.chooloo.www.callmanager.database.entity;

import android.content.Context;
import android.database.Cursor;
import android.provider.CallLog;

import com.chooloo.www.callmanager.util.ContactUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class RecentCall {

    // Attributes
    private Context mContext;
    private Contact mCaller;
    private String mNumber;
    private int mCallType;
    private String mCallDuration;
    private Date mCallDate;

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
        this.mCaller = ContactUtils.getContactByPhoneNumber(this.mContext, number);
        this.mCallType = type;
        this.mCallDuration = duration;
        this.mCallDate = date;
    }

    public RecentCall(Context context, Cursor cursor) {
        this.mContext = context;
        mNumber = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));
        if (mNumber != null) {
            mCaller = ContactUtils.getContactByPhoneNumber(context, mNumber);
        }
        mCallDuration = cursor.getString(cursor.getColumnIndex(CallLog.Calls.DURATION));
        mCallDate = new Date(cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DATE)));
        int callType = cursor.getInt(cursor.getColumnIndex(CallLog.Calls.TYPE));
        mCallType = callType;
    }

    public Contact getCaller() {
        return this.mCaller;
    }

    public String getCallerName() {
        if (this.mCaller != null) return this.mCaller.getName();
        else return null;
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
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yy hh:mm");
        String dateString = simpleDateFormat.format(this.mCallDate);
        return dateString;
    }
}
