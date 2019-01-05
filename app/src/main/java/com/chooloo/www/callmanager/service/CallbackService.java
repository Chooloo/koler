package com.chooloo.www.callmanager.service;

import android.content.Context;
import android.telecom.Call;
import android.util.Log;

import java.util.Dictionary;

public class CallbackService extends Call.Callback{

    private Context context;

    public CallbackService(Context context){
        this.context = context;
    }

    @Override
    public void onStateChanged(Call call, int state) {
//        int = Call.State
//        1   = Call.STATE_DIALING
//        2   = Call.STATE_RINGING
//        3   = Call.STATE_HOLDING
//        4   = Call.STATE_ACTIVE
//        7   = Call.STATE_DISCONNECTED
//        8   = Call.STATE_SELECT_PHONE_ACCOUNT
//        9   = Call.STATE_CONNECTING
//        10  = Call.STATE_DISCONNECTING
//        11  = Call.STATE_PULLING_CALL
        super.onStateChanged(call, state);
        String stringState = String.valueOf(state);
        Log.i("StateChanged",stringState);
    }

    @Override
    public void onDetailsChanged(Call call, Call.Details details) {
        super.onDetailsChanged(call, details);
        Log.i("DetailesChanged",details.toString());
    }


}
