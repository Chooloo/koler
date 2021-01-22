package com.chooloo.www.callmanager.service;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.telecom.Call;
import android.telecom.VideoProfile;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.chooloo.www.callmanager.entity.Contact;
import com.chooloo.www.callmanager.ui.base.BaseActivity;
import com.chooloo.www.callmanager.util.ContactUtils;
import com.chooloo.www.callmanager.util.PermissionUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import timber.log.Timber;

public class CallManager {

    public static Call sCall;

    public static void answer() {
        if (sCall != null) {
            sCall.answer(VideoProfile.STATE_AUDIO_ONLY);
        }
    }

    public static void reject() {
        if (sCall != null) {
            if (sCall.getState() == Call.STATE_RINGING) {
                sCall.reject(false, null);
            } else {
                sCall.disconnect();
            }
        }
    }

    public static void hold(boolean isHold) {
        if (sCall != null) {
            if (isHold) {
                sCall.hold();
            } else {
                sCall.unhold();
            }
        }
    }

    public static void keypad(char c) {
        if (sCall != null) {
            sCall.playDtmfTone(c);
            sCall.stopDtmfTone();
        }
    }

    public static void registerCallback(Call.Callback callback) {
        if (sCall != null) {
            sCall.registerCallback(callback);
        }
    }

    public static void unregisterCallback(Call.Callback callback) {
        if (sCall != null) {
            sCall.unregisterCallback(callback);
        }
    }

    public static int getState() {
        if (sCall != null) {
            return sCall.getState();
        }
        return Call.STATE_DISCONNECTED;
    }

    public static void call(@NonNull BaseActivity activity, @NonNull String number) {
        if (PermissionUtils.isDefaultDialer(activity)) {
            Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + Uri.encode(number)));
            activity.startActivity(callIntent);
        } else {
            PermissionUtils.ensureDefaultDialer(activity);
            activity.showError("Set Koler as your default dialer to make calls");
        }
    }

    public static void callVoicemail(@NonNull Context context) {
        try {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("voicemail:1"));
            context.startActivity(intent);
        } catch (SecurityException e) {
            Toast.makeText(context, "Couldn't start Voicemail", Toast.LENGTH_LONG).show();
            Timber.e(e);
        }
    }

    public static Contact getContact(Context context) {
        try {
            String number = URLDecoder.decode(sCall.getDetails().getHandle().toString(), "utf-8").replace("tel:", "");
            if (number.contains("voicemail")) {
                return Contact.VOICEMAIL;
            }
            return ContactUtils.lookupContact(context, number);
        } catch (UnsupportedEncodingException e) {
            return Contact.UNKNOWN;
        }
    }
}
