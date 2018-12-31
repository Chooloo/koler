package com.chooloo.www.callmanager.service;

import android.telecom.Call;
import android.telecom.VideoProfile;

public class CallManager {

    public static Call mCall;

    public static void updateCall(Call current_call){
        mCall = current_call;
    }

    public static Call.Details acceptCall(){
        mCall.answer(VideoProfile.STATE_AUDIO_ONLY);
        return mCall.getDetails();
    }

    public static void denyCall(){
        mCall.disconnect();
    }

}
