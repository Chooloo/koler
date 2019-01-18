package com.chooloo.www.callmanager;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.telecom.Call;
import android.telecom.VideoProfile;
import android.widget.Toast;

import com.chooloo.www.callmanager.activity.MainActivity;
import com.chooloo.www.callmanager.activity.OngoingCallActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.core.app.ActivityCompat;
import timber.log.Timber;

public class CallManager {
    public static Call sCall;

    /**
     * Answers incoming call
     */
    public static void sAnswer() {
        if (sCall != null) {
            sCall.answer(VideoProfile.STATE_AUDIO_ONLY);
        }
    }

    /**
     * Ends call
     * If call ended from the other side, disconnects
     */
    public static void sReject() {
        if (sCall != null) {
            if (sCall.getState() == Call.STATE_RINGING) {
                sCall.reject(false, null);
            } else {
                // Try both disconnecting and rejecting
                try {
                    sCall.disconnect();
                } catch (Exception e1) {
                    Timber.e("Couldn't disconnect call (Trying to reject): %s", e1);
                    try {
                        sCall.reject(false, null);
                    } catch (Exception e2) {
                        Timber.e("Couldn't end call: %s", e2);
                    }
                }
            }
        }
    }

    /**
     * Registers a Callback object to the current call
     *
     * @param callback the callback to register
     */
    public static void registerCallback(OngoingCallActivity.Callback callback) {
        if (sCall == null) return;
        sCall.registerCallback(callback);
    }

    /**
     * Unregisters the Callback from the current call
     *
     * @param callback the callback to unregister
     */
    public static void unregisterCallback(Call.Callback callback) {
        if (sCall == null) return;
        sCall.unregisterCallback(callback);
    }

    /**
     * Gets the phone number of the contact from the end side of the current call
     * in the case of a voicemail number, returns "Voicemail"
     *
     * @return String - phone number, or voicemail. if not recognized, return null.
     */
    public static String getDisplayName() {
        if (sCall == null) return null;
        String uri = sCall.getDetails().getHandle().toString();
        if (uri.contains("tel"))
            return uri.replace("tel:", "");
        if (uri.contains("voicemail"))
            return "Voicemail";
        return null;
    }

    /**
     * Gets the current state of the call from the Call object (named sCall)
     *
     * @return Call.State
     */
    public static int getState() {
        if (sCall == null) return Call.STATE_DISCONNECTED;
        return sCall.getState();
    }
}
