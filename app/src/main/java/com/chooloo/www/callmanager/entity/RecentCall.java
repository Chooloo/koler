package com.chooloo.www.callmanager.entity;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Date;

import static android.provider.CallLog.Calls.INCOMING_TYPE;
import static android.provider.CallLog.Calls.MISSED_TYPE;
import static android.provider.CallLog.Calls.OUTGOING_TYPE;
import static android.provider.CallLog.Calls.REJECTED_TYPE;
import static android.provider.CallLog.Calls.VOICEMAIL_TYPE;

public class RecentCall {

    private final long callId;
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

    public RecentCall(long id, String number, String duration, Date date, int type) {
        this.callId = id;
        this.number = number;
        this.callDuration = duration;
        this.callDate = date;
        this.callType = type;
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
}
