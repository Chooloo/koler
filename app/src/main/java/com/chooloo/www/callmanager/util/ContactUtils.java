package com.chooloo.www.callmanager.util;

import android.Manifest;
import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.PhoneLookup;
import android.provider.ContactsContract.CommonDataKinds.Phone;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.loader.content.Loader;

import com.chooloo.www.callmanager.database.entity.Contact;
import com.chooloo.www.callmanager.google.ContactsCursorLoader;

import java.sql.Time;

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

        // Build uri
        Cursor cursor = new ContactsCursorLoader(context, phoneNumber, null).loadInBackground();
        if (cursor == null) return new Contact(null, phoneNumber, null);

        if (cursor.moveToFirst())
            return new Contact(cursor);

        cursor.close();
        return new Contact(null, phoneNumber, null);
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

        // Build uri
        Cursor cursor = new ContactsCursorLoader(context, null, name).loadInBackground();
        if (cursor == null) return new Contact(name, null);

        // Return
        if (cursor.moveToFirst()) return new Contact(cursor);
        else {
            cursor.close();
            return new Contact(name, null);
        }
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
        Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));
        String[] projection = new String[]{PhoneLookup._ID};

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
     * @param number
     */
    public static void openContactToEditByNumber(Activity activity, String number) {
        try {
            long contactId = ContactUtils.getContactByPhoneNumber(activity, number).getContactId();
            Timber.i("CONTACT ID: " + contactId);
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

    /**
     * Open contact edit page in default contacts app by contact's id
     *
     * @param activity
     * @param contactId
     */
    public static void openContactToEditById(Activity activity, long contactId) {
        try {
            Intent intent = new Intent(Intent.ACTION_EDIT, ContactsContract.Contacts.CONTENT_URI);
            Uri contactUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId);
            intent.setData(contactUri);
            intent.putExtra("finishActivityOnSaveCompleted", true);
            //add the below line
            intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
            activity.startActivityForResult(intent, 1);
        } catch (Exception e) {
            Toast.makeText(activity, "Oops there was a problem trying to open the contact :(", Toast.LENGTH_SHORT).show();
            Timber.i("ERROR: " + e.getMessage());
        }
    }

    /**
     * Deletes contact by id
     *
     * @param activity
     * @param contactId
     */
    public static void deleteContactById(Activity activity, long contactId) {
        Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, Long.toString(contactId));
        int deleted = activity.getContentResolver().delete(uri, null, null);
        Toast.makeText(activity, "Contact Deleted", Toast.LENGTH_LONG).show();
    }

    /**
     * Sets the contact's favorite status by a given boolean (yes/no)
     *
     * @param activity
     * @param contactId
     * @param isSetFavorite
     */
    public static void setContactIsFavorite(Activity activity, String contactId, boolean isSetFavorite) {
        int num = isSetFavorite ? 1 : 0;
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            ContentValues v = new ContentValues();
            v.put(ContactsContract.Contacts.STARRED, num);
            activity.getContentResolver().update(ContactsContract.Contacts.CONTENT_URI, v, ContactsContract.Contacts._ID + "=?", new String[]{contactId + ""});
        }
    }
}
