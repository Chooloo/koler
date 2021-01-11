package com.chooloo.www.callmanager.service;

import android.content.Intent;
import android.telecom.Call;
import android.telecom.InCallService;

import com.chooloo.www.callmanager.ui.call.OngoingCallActivity;

public class CallService extends InCallService {

    @Override
    public void onCallAdded(Call call) {
        super.onCallAdded(call);
        CallManager.sCall = call;
        Intent intent = new Intent(this, OngoingCallActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onCallRemoved(Call call) {
        super.onCallRemoved(call);
        CallManager.sCall = null;
    }

}
