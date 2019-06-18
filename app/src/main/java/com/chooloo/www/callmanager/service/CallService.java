package com.chooloo.www.callmanager.service;

import android.content.Intent;
import android.telecom.Call;
import android.telecom.InCallService;

import com.chooloo.www.callmanager.ui.activity.OngoingCallActivity;
import com.chooloo.www.callmanager.util.CallManager;

public class CallService extends InCallService {

    /**
     * When call has been added
     *
     * @param call
     */
    @Override
    public void onCallAdded(Call call) {
        super.onCallAdded(call);
        Intent intent = new Intent(this, OngoingCallActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        CallManager.sCall = call;
    }

    /**
     * When call has been removed
     *
     * @param call
     */
    @Override
    public void onCallRemoved(Call call) {
        super.onCallRemoved(call);
        CallManager.sCall = null;
    }

}
