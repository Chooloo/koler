package com.chooloo.www.callmanager.util;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;
import android.telephony.PhoneNumberUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.chooloo.www.callmanager.cursorloader.ContactLookupCursorLoader;
import com.chooloo.www.callmanager.entity.Contact;

import static android.Manifest.permission.READ_CONTACTS;
import static com.chooloo.www.callmanager.util.PermissionUtils.checkPermission;

public class ContactUtils {

    public static Contact lookupContact(@NonNull Context context, @NonNull String phoneNumber) {
        if (!checkPermission(context, READ_CONTACTS, true)) {
            return Contact.UNKNOWN;
        }
        return ContactLookupCursorLoader.lookupContact(context, phoneNumber);
    }

    public static void openContact(@NonNull Activity activity, @NonNull Contact contact) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, String.valueOf(contact.getContactId())));
            activity.startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(activity, "Oops there was a problem trying to open the contact :(", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public static void addContact(@NonNull Activity activity, @NonNull Contact contact) {
        try {
            Intent intent = new Intent(Intent.ACTION_INSERT);
            intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
            intent.putExtra(ContactsContract.Intents.Insert.PHONE, contact.getNumber());
            activity.startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(activity, "Couldn't open add contact dialog", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    public static void editContact(@NonNull Activity activity, @NonNull Contact contact) {
        try {
            Intent intent = new Intent(Intent.ACTION_EDIT, ContactsContract.Contacts.CONTENT_URI);
            intent.setData(ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contact.getContactId()));
            activity.startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(activity, "Oops there was a problem trying to open the contact :(", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public static void deleteContact(@NonNull Activity activity, @NonNull Contact contact) {
        try {
            Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, Long.toString(contact.getContactId()));
            activity.getContentResolver().delete(uri, null, null);
            Toast.makeText(activity, "Contact Deleted", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(activity, "Contact couldn't be deleted", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    public static void smsContact(@NonNull Activity activity, @NonNull Contact contact) {
        Uri uri = Uri.parse(String.format("smsto:%s", PhoneNumberUtils.normalizeNumber(contact.getNumber())));
        Intent smsIntent = new Intent(Intent.ACTION_SENDTO, uri);
        activity.startActivity(smsIntent);
    }
}
