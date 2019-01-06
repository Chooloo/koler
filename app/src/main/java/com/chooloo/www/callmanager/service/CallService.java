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

    @Override
    public void onCallAdded(Call call) {
        super.onCallAdded(call);
        call.registerCallback(OngoingCallActivity.Callback(this));
        startActivity(new Intent(this, OngoingCallActivity.class));
        CallManager.sCall = call;
    }

    @Override
    public void onCallRemoved(Call call) {
        super.onCallRemoved(call);
        CallManager.sCall = null;
    }

}
