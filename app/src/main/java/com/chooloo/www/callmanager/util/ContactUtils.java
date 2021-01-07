package com.chooloo.www.callmanager.util;

import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

import android.widget.Toast;

import androidx.annotation.NonNull;

import com.chooloo.www.callmanager.cursorloader.ContactLookupCursorLoader;
import com.chooloo.www.callmanager.database.entity.Contact;
import com.chooloo.www.callmanager.cursorloader.ContactsCursorLoader;

import timber.log.Timber;

import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.WRITE_CONTACTS;
import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;
import static com.chooloo.www.callmanager.util.PermissionUtils.checkPermissionsGranted;

public class ContactUtils {

    // Constants
    public static final Contact UNKNOWN = new Contact("Unknown", "", null);
    public static final Contact VOICEMAIL = new Contact("Voicemail", "", null);
    public static final Contact PRIVATE = new Contact("Private Number", "", null);

    /**
     * Find and return contact object by a given name of phone number (or both)
     *
     * @param context     context activity
     * @param phoneNumber a parameter to search the contact by
     * @param name        a parameter to search the contact by
     * @return Contact object
     */
    public static Contact getContact(@NonNull Context context, String phoneNumber, String name) {
        // if phone number is empty, it's a private number
        if (phoneNumber.isEmpty() && (name == null || name.isEmpty())) return PRIVATE;
        // check for read contacts permission, ask for it if necessary
        if (!checkPermissionsGranted(context, new String[]{READ_CONTACTS}, true)) return UNKNOWN;
        // initiate contact cursor
        Cursor cursor = new ContactsCursorLoader(context, phoneNumber, name).loadInBackground();
        // handle null cursor
        if (cursor == null || cursor.getCount() == 0) return new Contact(name, phoneNumber);
        // cursor isn't empty, get the first result
        cursor.moveToFirst();
        return new Contact(cursor);
    }

    public static Contact lookupContact(@NonNull Context context, @NonNull String phoneNumber) {
        if (!checkPermissionsGranted(context, new String[]{READ_CONTACTS}, true)) {
            return UNKNOWN;
        }

        Contact contact = new ContactLookupCursorLoader(context, phoneNumber).loadContact();
        if (contact == null) {
            return new Contact(null, phoneNumber);
        }

        return contact;
    }

    /**
     * Opens 'Add Contact' dialog from default os
     *
     * @param number
     */
    public static void openAddContact(Activity activity, String number) {
        try {
            Intent addContactIntent = new Intent(Intent.ACTION_INSERT); // initiate intent
            addContactIntent.setType(ContactsContract.Contacts.CONTENT_TYPE); // add type
            addContactIntent.putExtra(ContactsContract.Intents.Insert.PHONE, number); // add number
            int PICK_CONTACT = 100; // Unique number to return when done with intent
            activity.startActivityForResult(addContactIntent, PICK_CONTACT); // start intent
        } catch (Exception e) {
            Toast.makeText(activity, "Couldn't open add contact dialog", Toast.LENGTH_LONG).show();
            Timber.e("ERROR: " + e.getMessage());
        }
    }


    /**
     * Open contact in default contacts app by the contact's id
     *
     * @param activity
     * @param contactId
     */
    public static void openContact(Activity activity, long contactId) {
        try {
            // new intent to view contact in contacts
            Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, String.valueOf(contactId));
            intent.setData(uri);
            activity.startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(activity, "Oops there was a problem trying to open the contact :(", Toast.LENGTH_SHORT).show();
            Timber.e("ERROR: " + e.getMessage());
        }
    }


    /**
     * Open contact edit page in default contacts app by contact's id
     *
     * @param activity
     * @param contactId
     */
    public static void openContactToEdit(Activity activity, long contactId) {
        try {
            Intent intent = new Intent(Intent.ACTION_EDIT, ContactsContract.Contacts.CONTENT_URI);
            intent.setData(ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId));
            intent.putExtra("finishActivityOnSaveCompleted", true);
            //add the below line?
            intent.addFlags(FLAG_ACTIVITY_CLEAR_TOP);
            activity.startActivityForResult(intent, 1);
        } catch (Exception e) {
            Toast.makeText(activity, "Oops there was a problem trying to open the contact :(", Toast.LENGTH_SHORT).show();
            Timber.e("ERROR: " + e.getMessage());
        }
    }

    /**
     * Delete contact from device by the contact's id
     *
     * @param activity  context activity
     * @param contactId id of the contact to delete
     */
    public static void deleteContact(Activity activity, long contactId) {
        try {
            Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, Long.toString(contactId));
            activity.getContentResolver().delete(uri, null, null);
            Toast.makeText(activity, "Contact Deleted", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(activity, "Contact couldn't be deleted", Toast.LENGTH_LONG).show();
            Timber.e("ERROR: " + e.getMessage());
        }
    }

    /**
     * Sets the contact's favorite status by a given boolean (yes/no)
     *
     * @param activity
     * @param contactId
     * @param isSetFavorite
     */
    public static void setContactIsFavorite(Activity activity, String contactId, boolean isSetFavorite) {
        try {
            int num = isSetFavorite ? 1 : 0; // convert boolean to num
            if (PermissionUtils.checkPermissionsGranted(activity, new String[]{WRITE_CONTACTS}, true)) {
                ContentValues v = new ContentValues();
                v.put(ContactsContract.Contacts.STARRED, num);
                activity.getContentResolver().update(ContactsContract.Contacts.CONTENT_URI, v, ContactsContract.Contacts._ID + "=?", new String[]{contactId + ""});
            }
        } catch (Exception e) {
            Toast.makeText(activity, "Couldn't save preference", Toast.LENGTH_LONG).show();
            Timber.e("ERROR: " + e.getMessage());
        }
    }
}
