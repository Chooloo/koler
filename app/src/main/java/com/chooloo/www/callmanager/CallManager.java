package com.chooloo.www.callmanager;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.telecom.Call;
import android.telecom.VideoProfile;
import android.util.Log;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.chooloo.www.callmanager.activity.MainActivity;
import com.chooloo.www.callmanager.activity.OngoingCallActivity;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.core.app.ActivityCompat;

import static android.content.ContentValues.TAG;

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
            if (sCall.getState() == Call.STATE_ACTIVE) {
                sCall.disconnect();
            } else {
                sCall.reject(false, null);
            }
        }
    }

    /**
     * Registers a Callback object to the current call
     *
     * @param callback
     */
    public static void registerCallback(OngoingCallActivity.Callback callback) {
        if (sCall == null) return;
        sCall.registerCallback(callback);
    }

    /**
     * Unregisters the Callback from the current call
     *
     * @param callback
     */
    public static void unregisterCallback(Call.Callback callback) {
        if (sCall == null) return;
        sCall.unregisterCallback(callback);
    }

    /**
     * Returns a Map (Dictionary) of all the contacts by (name,number)
     *
     * @param context
     * @return Map<String                                                                                                                               ,                                                                                                                               String> of all the contacts currently on the target device
     */
    public static Map<String, String> getContacts(Context context) {
        Map<String, String> contacts = new HashMap<String, String>();
//        ArrayList<String> contacts = new ArrayList<String>();
        ContentResolver cr = context.getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);
        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {
                String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                if (Integer.parseInt(cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        String contactName = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                        String phoneNum = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        contacts.put(contactName, phoneNum);
                    }
                    pCur.close();
                }
            }
        }
        return contacts;
    }

    /**
     * Gets the contact's names who contains in their phone number the given number
     *
     * @param context
     * @param num
     * @return ArrayList<String> of the contacts who has the given number in their phone number
     */
    public static Map<String, String> getContactsByNum(Context context, String num) {
        Map<String, String> matchContacts = new HashMap<String, String>();
        Map<String, String> contacts = getContacts(context);
        for (Map.Entry<String, String> contact : contacts.entrySet()) {
            if (contact.getValue().contains(num)) {
                matchContacts.put(contact.getKey(), contact.getValue());
            }
        }
        return matchContacts;
    }

    /**
     * Get the current contact's name from the end side of the current call
     *
     * @param context
     * @return String - the contact's name
     */
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

    public static void getContactsIntoList() {

    }

    /**
     * Gets the phone number of the contact from the end side of the current call
     * Incase of a voicemail number, returns "Voicemail
     *
     * @return String - phone number / Voicemail / null
     */
    public static String getPhoneNumber() {
        if (sCall == null) return "";
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
