package com.chooloo.www.callmanager.util;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

import com.chooloo.www.callmanager.database.entity.Contact;

import androidx.core.app.ActivityCompat;

public class ContactUtils {

    public static final Contact UNKNOWN = new Contact("Unknown", "", null);
    public static final Contact VOICEMAIL = new Contact("Voicemail", "", null);

    /**
     * Get the current contact's name from the end side of the current call
     *
     * @return the contact's name
     */
    public static Contact getContactByPhoneNumber(Context context, String phoneNumber) {

        //Check for permission to read contacts
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            //Don't prompt the user now, they are getting a call
            return null;
        }

        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
        String[] projection = new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME, ContactsContract.PhoneLookup.PHOTO_URI};
        Contact contact;

        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) return null;
        if (cursor.moveToFirst()) {
            contact = new Contact(cursor.getString(0), phoneNumber, cursor.getString(1));
        } else {
            return null;
        }
        cursor.close();

        return contact;
    }
}
