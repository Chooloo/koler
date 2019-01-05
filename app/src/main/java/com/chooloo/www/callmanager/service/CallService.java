package com.chooloo.www.callmanager.service;

import android.content.Context;
import android.content.Intent;
import android.telecom.Call;
import android.telecom.InCallService;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.chooloo.www.callmanager.CallManager;
import com.chooloo.www.callmanager.activity.OngoingCallActivity;

public class CallService extends InCallService {

    public static Context mContext;
    TelephonyManager mTelephonyManager = (TelephonyManager)getApplicationContext().getSystemService(TELEPHONY_SERVICE);

    public PhoneStateListener mPhoneStateListener = new PhoneStateListener(){

        @Override
        public void onCallStateChanged(int state, String phoneNumber) {
            CallManager.sPhoneNumber = phoneNumber;
            switch (state){
                case TelephonyManager.CALL_STATE_IDLE:
                    CallManager.sCallState = "IDLE";
                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                    CallManager.sCallState = "RINGING";
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    CallManager.sCallState = "OFFHOOK";
                    break;
            }
        }
    };

    @Override
    public void onCallAdded(Call call) {
        mTelephonyManager.listen(mPhoneStateListener,
                PhoneStateListener.LISTEN_CALL_STATE
                | PhoneStateListener.LISTEN_CELL_INFO // Requires API 17
                | PhoneStateListener.LISTEN_CELL_LOCATION
                | PhoneStateListener.LISTEN_DATA_ACTIVITY
                | PhoneStateListener.LISTEN_DATA_CONNECTION_STATE
                | PhoneStateListener.LISTEN_SERVICE_STATE
                | PhoneStateListener.LISTEN_SIGNAL_STRENGTHS
                | PhoneStateListener.LISTEN_CALL_FORWARDING_INDICATOR
                | PhoneStateListener.LISTEN_MESSAGE_WAITING_INDICATOR);
        super.onCallAdded(call);
        startActivity(new Intent(this, OngoingCallActivity.class));
        CallManager.sCall = call;
    }

    @Override
    public void onCallRemoved(Call call) {
        super.onCallRemoved(call);
        CallManager.sCall = null;
    }

}
