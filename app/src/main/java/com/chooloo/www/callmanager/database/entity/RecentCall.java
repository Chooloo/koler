package com.chooloo.www.callmanager.database.entity;

import android.content.Context;
import android.database.Cursor;
import android.provider.CallLog;

import com.chooloo.www.callmanager.adapter.RecentsAdapter;
import com.chooloo.www.callmanager.util.ContactUtils;

import java.util.Date;

public class RecentCall {

    // Attributes
    private Context mContext;
    private Contact mCaller;
    private String mNumber;
    private String mCallType;
    private String mCallDuration;
    private long mCallDate;

    // Call Types
    public static final String mOutgoingCall = "OUTGOING_CALL";
    public static final String mIncomingCall = "INCOMING_CALL";
    public static final String mMissedCall = "MISSED_CALL";

    /**
     * Constructor
     *
     * @param number   caller's number
     * @param type     call's type (out/in/missed)
     * @param duration call's duration
     * @param date     call's date
     */
    public RecentCall(Context context, String number, int type, String duration, long date) {
        this.mContext = context;
        this.mNumber = number;
        this.mCaller = ContactUtils.getContactByPhoneNumber(this.mContext, number);
        this.mCallType = getTypeByInt(type);
        this.mCallDuration = duration;
        this.mCallDate = date;
    }

    public RecentCall(Context context, Cursor cursor) {
        this.mContext = context;
        this.mNumber = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));
        this.mCaller = ContactUtils.getContactByPhoneNumber(this.mContext, this.mNumber);
        this.mCallDuration = cursor.getString(cursor.getColumnIndex(CallLog.Calls.DURATION));
        this.mCallDate = cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DATE));
        int callType = cursor.getInt(cursor.getColumnIndex(CallLog.Calls.TYPE));
        switch (callType) {
            case 0:
                this.mCallType = mOutgoingCall;
            case 1:
                this.mCallType = mIncomingCall;
            case 2:
                this.mCallType = mMissedCall;
            default:
                this.mCallType = null;
        }
    }

    public Contact getCaller() {
        return this.mCaller;
    }

    public String getCallerName() {
        return this.mCaller.getName();
    }

    public String getCallerNumber() {
        return this.mNumber;
    }

    public String getCallType() {
        return this.mCallType;
    }

    public String getCallDuration() {
        return this.mCallDuration;
    }

    public long getCallDate() {
        return this.mCallDate;
    }

    private String getTypeByInt(int type) {
        switch (type) {
            case CallLog.Calls.OUTGOING_TYPE:
                return RecentCall.mOutgoingCall;
            case CallLog.Calls.INCOMING_TYPE:
                return RecentCall.mIncomingCall;
            case CallLog.Calls.MISSED_TYPE:
                return RecentCall.mMissedCall;
            default:
                return null;
        }
    }
}
