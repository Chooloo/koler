package com.chooloo.www.callmanager;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.telecom.Call;
import android.telecom.TelecomManager;
import android.telecom.VideoProfile;
import android.text.method.TextKeyListener;

import androidx.core.app.ActivityCompat;

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

//    ******** This shit isnt needed, delete it, look at CallService.java for the right way
//    public static void registerCallback(Call.Callback callback) {
//        if (sCall == null) return;
//        sCall.registerCallback(callback);
//    }
//
//    public static void unregisterCallback(Call.Callback callback) {
//        if (sCall == null) return;
//        sCall.unregisterCallback(callback);
//    }

    public static String getContactName(Context context) {
        //Check for permission to read contacts
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            //Don't prompt the user now, they are getting a call
            return null;
        }
        if (sCall == null)
            return null;

        String phoneNumber = getPhoneNumber();
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
        String[] projection = new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME};
        String contactName;

        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) return null;
        if (cursor.moveToFirst()) {
            contactName = cursor.getString(0);
        } else {
            return null;
        }
        cursor.close();

        return contactName;
    }

    public static String getPhoneNumber() {
        if (sCall == null) return "";
        String uri = sCall.getDetails().getHandle().toString();
        if (uri.contains("tel"))
            return uri.replace("tel:", "");
        if (uri.contains("voicemail"))
            return "Voicemail";
        return null;
    }
}
