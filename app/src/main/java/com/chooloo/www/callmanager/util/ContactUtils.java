package com.chooloo.www.callmanager.util;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;
import android.telephony.PhoneNumberUtils;

import androidx.annotation.NonNull;

import com.chooloo.www.callmanager.cursorloader.ContactLookupCursorLoader;
import com.chooloo.www.callmanager.entity.Contact;
import com.chooloo.www.callmanager.ui.base.BaseActivity;

import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.WRITE_CONTACTS;
import static com.chooloo.www.callmanager.util.PermissionUtils.checkPermission;

public class ContactUtils {

    public final static int PERMISSION_RC_WRITE_CONTACTS = 1;

    public static Contact lookupContact(@NonNull Context context, @NonNull String phoneNumber) {
        if (!checkPermission(context, READ_CONTACTS, true)) {
            return Contact.UNKNOWN;
        }
        return ContactLookupCursorLoader.lookupContact(context, phoneNumber);
    }

    public static void openContact(@NonNull BaseActivity activity, @NonNull Contact contact) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, String.valueOf(contact.getContactId())));
        activity.startActivity(intent);
    }

    public static void addContact(@NonNull BaseActivity activity, @NonNull Contact contact) {
        Intent intent = new Intent(Intent.ACTION_INSERT);
        intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
        intent.putExtra(ContactsContract.Intents.Insert.PHONE, contact.getNumber());
        activity.startActivity(intent);
    }

    public static void editContact(@NonNull BaseActivity activity, @NonNull Contact contact) {
        Intent intent = new Intent(Intent.ACTION_EDIT, ContactsContract.Contacts.CONTENT_URI);
        intent.setData(ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contact.getContactId()));
        activity.startActivity(intent);
    }

    public static void deleteContact(@NonNull BaseActivity activity, @NonNull Contact contact) {
        if (activity.hasPermission(WRITE_CONTACTS)) {
            Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, Long.toString(contact.getContactId()));
            activity.getContentResolver().delete(uri, null, null);
            activity.showMessage("Contact Deleted");
        } else {
            activity.showError("Give me a permission and try again");
            activity.askForPermission(WRITE_CONTACTS, PERMISSION_RC_WRITE_CONTACTS);
        }
    }

    public static void smsContact(@NonNull Activity activity, @NonNull Contact contact) {
        Uri uri = Uri.parse(String.format("smsto:%s", PhoneNumberUtils.normalizeNumber(contact.getNumber())));
        Intent smsIntent = new Intent(Intent.ACTION_SENDTO, uri);
        activity.startActivity(smsIntent);
    }
}
