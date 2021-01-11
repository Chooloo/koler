package com.chooloo.www.callmanager.service;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.telecom.Call;
import android.telecom.VideoProfile;
import android.widget.Toast;

import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.entity.Contact;
import com.chooloo.www.callmanager.util.ContactUtils;
import com.chooloo.www.callmanager.util.PermissionUtils;
import com.chooloo.www.callmanager.util.PreferencesManager;

import org.jetbrains.annotations.NotNull;

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

    private static int getSimSelection(Context context) {
        return PreferencesManager.getInstance(context).getInt(R.string.pref_sim_select_key, 0);
    }

    public static boolean callVoicemail(Context context) {
        try {
            context.startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("voicemail:1")));
            return true;
        } catch (SecurityException e) {
            Toast.makeText(context, "Couldn't start Voicemail", Toast.LENGTH_LONG).show();
            Timber.e(e);
            return false;
        }
    }

    public static void addCall(Call call) {
        if (sCall != null) sCall.conference(call);
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

    public static Contact getDisplayContact(Context context) {
        try {
            String number = URLDecoder.decode(sCall.getDetails().getHandle().toString(), "utf-8").replace("tel:", "");
            if (number.contains("voicemail")) {
                return ContactUtils.VOICEMAIL;
            }
            return ContactUtils.getContact(context, number, null); // get the contacts with the number
        } catch (Exception e) {
            return ContactUtils.UNKNOWN;
        }
    }

    public static int getState() {
        if (sCall != null) {
            return sCall.getState();
        }
        return Call.STATE_DISCONNECTED;
    }

    public static void call(@NotNull Activity activity, @NotNull String number) {
        if (!PermissionUtils.checkDefaultDialer(activity)) {
            Toast.makeText(activity, "Set Koler as your default dialer to make calls", Toast.LENGTH_LONG).show();
            return;
        }

        try {
            Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel: " + Uri.encode(number)));
            activity.startActivity(callIntent);

        } catch (SecurityException e) {
            Toast.makeText(activity, "Couldn't make the call due to security reasons", Toast.LENGTH_LONG).show();
            e.printStackTrace();

        } catch (NullPointerException e) {
            Toast.makeText(activity, "No phone number detected", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
}
