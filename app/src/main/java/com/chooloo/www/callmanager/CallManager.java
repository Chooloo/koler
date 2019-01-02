package com.chooloo.www.callmanager;

import android.telecom.Call;
import android.telecom.VideoProfile;

public class CallManager {
    public static Call sCall;

    public static void sAnswer() {
        if (sCall != null) {
            sCall.answer(VideoProfile.STATE_AUDIO_ONLY);
        }
    }

    public static void sReject() {
        if (sCall != null) {
            if (sCall.getState() == Call.STATE_ACTIVE) {
                sCall.disconnect();
            } else {
                sCall.reject(false, null);
            }
        }
    }

    public static String getPhoneNumber() {
        if (sCall != null) {
            return sCall.getDetails().getHandle().toString().substring(4);
        }
        return "";
    }
}
