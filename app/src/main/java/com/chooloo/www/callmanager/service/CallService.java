package com.chooloo.www.callmanager.service;

import android.content.Intent;
import android.telecom.Call;
import android.telecom.CallAudioState;
import android.telecom.InCallService;
import android.telecom.TelecomManager;
import android.telecom.VideoProfile;
import android.util.Log;

import com.chooloo.www.callmanager.CallManager;
import com.chooloo.www.callmanager.activity.OngoingCallActivity;

public class CallService extends InCallService {

    public static Call sOngoingCall = null;

    @Override
    public void onCallAdded(Call call) {
        super.onCallAdded(call);
        startActivity(new Intent(this, OngoingCallActivity.class));
        sOngoingCall = call;
        CallManager.updateCall(call);
    }

    @Override
    public void onCallRemoved(Call call) {
        super.onCallRemoved(call);
        sOngoingCall = null;
    }

    Call.Callback callCallback = new Call.Callback() {
        @Override
        public void onStateChanged(Call call, int state) {
            super.onStateChanged(call, state);
            // Send updates to the activity
        }
    };

}
