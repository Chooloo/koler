package com.chooloo.www.callmanager.cursorloader;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.PhoneLookup;
import android.telephony.PhoneNumberUtils;

import androidx.annotation.NonNull;
import androidx.loader.content.CursorLoader;

import com.chooloo.www.callmanager.entity.Contact;

public class ContactLookupCursorLoader extends CursorLoader {
    public static final String COLUMN_ID = PhoneLookup.CONTACT_ID;
    public static final String COLUMN_NAME = PhoneLookup.DISPLAY_NAME_PRIMARY;
    public static final String COLUMN_NUMBER = PhoneLookup.NUMBER;

    protected static final String CONTACT_LOOKUP_ORDER = PhoneLookup.DISPLAY_NAME_PRIMARY + " ASC";
    protected static final String CONTACT_LOOKUP_SELECTION = ContactsContract.Contacts.DISPLAY_NAME_PRIMARY + " IS NOT NULL";
    protected static final String[] CONTACT_LOOKUP_PROJECTION = new String[]{
            COLUMN_ID,
            COLUMN_NAME,
            COLUMN_NUMBER
    };

    public static Contact lookupContact(@NonNull Context context, @NonNull String phoneNumber) {
        return new ContactLookupCursorLoader(context, phoneNumber).loadContact();
    }

    public ContactLookupCursorLoader(@NonNull Context context, @NonNull String phoneNumber) {
        super(
                context,
                buildUri(phoneNumber),
                CONTACT_LOOKUP_PROJECTION,
                CONTACT_LOOKUP_SELECTION,
                null,
                CONTACT_LOOKUP_ORDER
        );
    }

    public Contact loadContact() {
        Cursor cursor = super.loadInBackground();
        if (cursor == null || cursor.getCount() == 0) {
            return Contact.unknownContact();
        }

        cursor.moveToFirst();
        String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
        String number = cursor.getString(cursor.getColumnIndex(COLUMN_NUMBER));
        cursor.close();
        return new Contact(name, number);
    }

    private static Uri buildUri(String phoneNumber) {
        Uri.Builder builder = PhoneLookup.CONTENT_FILTER_URI.buildUpon();
        builder.appendPath(PhoneNumberUtils.normalizeNumber(phoneNumber));
        builder.appendQueryParameter(ContactsContract.STREQUENT_PHONE_ONLY, "true");
        builder.appendQueryParameter(ContactsContract.REMOVE_DUPLICATE_ENTRIES, "true");
        return builder.build();
    }
}