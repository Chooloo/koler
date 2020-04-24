package com.chooloo.www.callmanager.util;

import android.Manifest;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.chooloo.www.callmanager.database.entity.Contact;

import timber.log.Timber;

public class ContactUtils {

    // Constants
    public static final Contact UNKNOWN = new Contact("Unknown", "", null);
    public static final Contact VOICEMAIL = new Contact("Voicemail", "", null);
    public static final Contact ERROR = new Contact("Error", "", null);

    /**
     * Returns a contact by a given phone number
     *
     * @param context
     * @param phoneNumber
     * @return Contact
     */
    public static Contact getContactByPhoneNumber(@NonNull Context context, @NonNull String phoneNumber) {

        if (phoneNumber.isEmpty()) return null;

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
            contact = new Contact(null, phoneNumber, null);
            return contact;
        }
        cursor.close();

        return contact;
    }

    /**
     * Returns a contact by a given name
     * (almost identical to the previous method (above) but filters the name instead of
     * the phone number)
     *
     * @param context
     * @param name
     * @return Contact
     */
    public static Contact getContactByName(@NonNull Context context, @NonNull String name) {

        if (name.isEmpty()) return null;

        //Check for permission to read contacts
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            //Don't prompt the user now, they are getting a call
            return null;
        }

        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(name));
        String[] projection = new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME, ContactsContract.PhoneLookup.PHOTO_URI};
        Contact contact;

        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) return null;
        if (cursor.moveToFirst()) {
            contact = new Contact(cursor.getString(0), name, cursor.getString(1));
        } else {
            return null;
        }
        cursor.close();

        return contact;
    }

    /**
     * Opens 'Add Contact' dialog from default os
     *
     * @param number
     */
    public static void addContactIntent(Activity activity, String number) {
        // Initiate intent
        Intent addContactIntent = new Intent(Intent.ACTION_INSERT);
        addContactIntent.setType(ContactsContract.Contacts.CONTENT_TYPE);

        // Insert number
        addContactIntent.putExtra(ContactsContract.Intents.Insert.PHONE, number);

        // Unique number to return when done with intent
        int PICK_CONTACT = 100;

        activity.startActivityForResult(addContactIntent, PICK_CONTACT);
    }

    /**
     * Opens a contact in the default contacts app by a given number
     *
     * @param activity
     * @param number
     */
    public static void openContactByNumber(Activity activity, String number) {
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));
        String[] projection = new String[]{ContactsContract.PhoneLookup._ID};

        Cursor cur = activity.getContentResolver().query(uri, projection, null, null, null);

        // if other contacts have that phone as well, we simply take the first contact found.
        if (cur != null && cur.moveToNext()) {
            Long id = cur.getLong(0);

            Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri contactUri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, String.valueOf(id));
            intent.setData(contactUri);
            activity.startActivity(intent);

            cur.close();
        }
    }

    /**
     * Open contact in default contacts app by the contact's id
     *
     * @param activity
     * @param contactId
     */
    public static void openContactById(Activity activity, long contactId) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, String.valueOf(contactId));
            intent.setData(uri);
            activity.startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(activity, "Oops there was a problem trying to open the contact :(", Toast.LENGTH_SHORT).show();
            Timber.i("ERROR: " + e.getMessage());
        }
    }

    /**
     * Open contact edit page in default contacts app by contact's id
     *
     * @param activity
     * @param contactId
     */
    public static void openContactToEditById(Activity activity, long contactId) {
        try {
            Uri mUri = ContentUris.withAppendedId(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    contactId);
            Intent intent = new Intent(Intent.ACTION_EDIT);
            intent.setDataAndType(mUri, ContactsContract.Contacts.CONTENT_ITEM_TYPE);
            intent.putExtra("finishActivityOnSaveCompleted", true);

            //add the below line
            intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
            activity.startActivityForResult(intent, 1);
        } catch (Exception e) {
            Toast.makeText(activity, "Oops there was a problem trying to open the contact :(", Toast.LENGTH_SHORT).show();
            Timber.i("ERROR: " + e.getMessage());
        }
    }
}
