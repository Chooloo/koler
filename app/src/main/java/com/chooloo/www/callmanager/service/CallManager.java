package com.chooloo.www.callmanager.service;

import android.telecom.Call;
import android.telecom.VideoProfile;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

public class CallManager extends PhoneStateListener {

    public static boolean mOnCall = false;
    public static String mStatus;
    public static Call mCall;

    @Override
    public void onCallStateChanged(int state, String phoneNumber) {
        switch (state) {
            case TelephonyManager.CALL_STATE_RINGING:
                mStatus = "RINGING";
                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:
                mStatus = "OFFHOOK";
                mOnCall = true;
                break;
            case TelephonyManager.CALL_STATE_IDLE:
                mStatus = "IDLE";
                if (mOnCall) {
                    mOnCall = false;
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                    System.exit(0);
                }
        }
    }

    public static void updateCall(Call current_call) {
        mCall = current_call;
    }

    public static Call.Details acceptCall() {
        mCall.answer(VideoProfile.STATE_AUDIO_ONLY);
        return mCall.getDetails();
    }

    public static void denyCall() {
        mCall.disconnect();
    }

}
