package com.chooloo.www.callmanager.cursorloader;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.PhoneLookup;
import android.telephony.PhoneNumberUtils;

import androidx.annotation.NonNull;
import androidx.loader.content.CursorLoader;

import com.chooloo.www.callmanager.database.entity.Contact;

public class ContactLookupCursorLoader extends CursorLoader {
    private static final String COLUMN_ID = PhoneLookup.CONTACT_ID;
    private static final String COLUMN_NAME = PhoneLookup.DISPLAY_NAME_PRIMARY;
    private static final String COLUMN_NUMBER = PhoneLookup.NUMBER;

    public ContactLookupCursorLoader(@NonNull Context context, @NonNull String phoneNumber) {
        super(
                context,
                buildUri(context, phoneNumber),
                new String[]{
                        COLUMN_ID,
                        COLUMN_NAME,
                        COLUMN_NUMBER,
                },
                ContactsCursorLoader.getWhere(),
                null,
                PhoneLookup.DISPLAY_NAME_PRIMARY + " ASC"
        );
    }

    public Contact loadContact() {
        Cursor cursor = super.loadInBackground();
        if (cursor == null || cursor.getCount() == 0) {
            return null;
        }

        cursor.moveToFirst();

        Contact contact = new Contact(
                cursor.getString(cursor.getColumnIndex(COLUMN_NAME)),
                cursor.getString(cursor.getColumnIndex(COLUMN_NUMBER))
        );

        cursor.close();

        return contact;
    }

    private static Uri buildUri(Context context, String phoneNumber) {
        Uri.Builder builder = PhoneLookup.CONTENT_FILTER_URI.buildUpon();

        builder.appendPath(PhoneNumberUtils.normalizeNumber(phoneNumber));
        builder.appendQueryParameter(ContactsContract.STREQUENT_PHONE_ONLY, "true");
        builder.appendQueryParameter(ContactsContract.REMOVE_DUPLICATE_ENTRIES, "true");
        builder.appendQueryParameter(Contacts.EXTRA_ADDRESS_BOOK_INDEX, "true");

        return builder.build();
    }
}
