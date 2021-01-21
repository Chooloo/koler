package com.chooloo.www.callmanager.util;

import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.chooloo.www.callmanager.cursorloader.ContactLookupCursorLoader;
import com.chooloo.www.callmanager.entity.Contact;

import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.WRITE_CONTACTS;
import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;
import static com.chooloo.www.callmanager.util.PermissionUtils.checkPermissionsGranted;

public class ContactUtils {

    public static Contact lookupContact(@NonNull Context context, @NonNull String phoneNumber) {
        if (!checkPermissionsGranted(context, new String[]{READ_CONTACTS}, true)) {
            return Contact.UNKNOWN;
        }
        return ContactLookupCursorLoader.lookupContact(context, phoneNumber);
    }

    public static void openAddContactDialog(Activity activity, String number) {
        try {
            Intent addContactIntent = new Intent(Intent.ACTION_INSERT); // initiate intent
            addContactIntent.setType(ContactsContract.Contacts.CONTENT_TYPE); // add type
            addContactIntent.putExtra(ContactsContract.Intents.Insert.PHONE, number); // add number
            int PICK_CONTACT = 100; // Unique number to return when done with intent
            activity.startActivityForResult(addContactIntent, PICK_CONTACT); // start intent
        } catch (Exception e) {
            Toast.makeText(activity, "Couldn't open add contact dialog", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    public static void openContact(Activity activity, long contactId) {
        try {
            // new intent to view contact in contacts
            Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, String.valueOf(contactId));
            intent.setData(uri);
            activity.startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(activity, "Oops there was a problem trying to open the contact :(", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

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
            e.printStackTrace();
        }
    }

    public static void deleteContact(Activity activity, long contactId) {
        try {
            Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, Long.toString(contactId));
            activity.getContentResolver().delete(uri, null, null);
            Toast.makeText(activity, "Contact Deleted", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(activity, "Contact couldn't be deleted", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }


    public static void setContactIsFavorite(Activity activity, String contactId, boolean isSetFavorite) {
        try {
            if (PermissionUtils.checkPermissionsGranted(activity, new String[]{WRITE_CONTACTS}, true)) {
                ContentValues v = new ContentValues();
                v.put(ContactsContract.Contacts.STARRED, isSetFavorite ? 1 : 0);
                activity.getContentResolver().update(ContactsContract.Contacts.CONTENT_URI, v, ContactsContract.Contacts._ID + "=?", new String[]{contactId + ""});
            }
        } catch (Exception e) {
            Toast.makeText(activity, "Couldn't save preference", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
}
