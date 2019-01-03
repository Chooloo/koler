package com.chooloo.www.callmanager;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.telecom.Call;
import android.telecom.TelecomManager;
import android.telecom.VideoProfile;
import android.text.method.TextKeyListener;

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

    public static String getContactName(Context context) {
        if (sCall != null) {
            String phoneNumber = getPhoneNumber();

            Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));

            String[] projection = new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME};

            String contactName = "";
            Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    contactName = cursor.getString(0);
                }
                cursor.close();
            }

            return contactName;
        }
        return "";
    }

    public static String getPhoneNumber() {
        if (sCall != null) {
            return sCall.getDetails().getHandle().toString().substring(4);
        }
        return "";
    }
}
