package com.chooloo.www.callmanager.service;

import android.content.Intent;
import android.telecom.Call;
import android.telecom.InCallService;

import com.chooloo.www.callmanager.util.bla.CallManager;
import com.chooloo.www.callmanager.ui.activity.OngoingCallActivity;

public class CallService extends InCallService {

    @Override
    public void onCallAdded(Call call) {
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
