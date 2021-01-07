/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.chooloo.www.callmanager.cursorloader;

import android.content.Context;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.telephony.PhoneNumberUtils;

import androidx.loader.content.CursorLoader;

import static android.provider.ContactsContract.Contacts;

public class ContactsCursorLoader extends CursorLoader {

    // Columns
    public static String COLUMN_ID = Phone.CONTACT_ID;
    public static String COLUMN_NAME = Contacts.DISPLAY_NAME_PRIMARY;
    public static String COLUMN_THUMBNAIL = Contacts.PHOTO_THUMBNAIL_URI;
    public static String COLUMN_NUMBER = Phone.NUMBER;
    public static String COLUMN_STARRED = Phone.STARRED;

    private static String CONTACTS_ORDER = Contacts.SORT_KEY_PRIMARY + " ASC";

    /**
     * Cursor selection string
     */
    public static final String[] CONTACTS_PROJECTION_DISPLAY_NAME_PRIMARY =
            new String[]{
                    COLUMN_ID,
                    COLUMN_NAME,
                    COLUMN_THUMBNAIL,
                    COLUMN_NUMBER,
                    COLUMN_STARRED
            };

    /**
     * Constructor
     *
     * @param context     Context
     * @param phoneNumber contact's number
     * @param contactName contect's name
     */
    public ContactsCursorLoader(Context context, String phoneNumber, String contactName) {
        super(
                context,
                buildUri(phoneNumber, contactName),
                CONTACTS_PROJECTION_DISPLAY_NAME_PRIMARY,
                getWhere(),
                null,
                CONTACTS_ORDER);
    }


    /**
     * Get a filter string
     *
     * @return String The selection string
     */
    static String getWhere() {
        return "(" + Phone.DISPLAY_NAME_PRIMARY + " IS NOT NULL" +
                " OR " + Phone.DISPLAY_NAME_ALTERNATIVE + " IS NOT NULL" + ")" +
                " AND " + Phone.HAS_PHONE_NUMBER + "=1" +
                " AND (" + ContactsContract.RawContacts.ACCOUNT_NAME + " IS NULL" +
                " OR ( " + ContactsContract.RawContacts.ACCOUNT_TYPE + " NOT LIKE '%whatsapp%'" +
                " AND " + ContactsContract.RawContacts.ACCOUNT_TYPE + " NOT LIKE '%tachyon%'" + "))";
    }


    /**
     * Builds contact uri by given name and phone number
     *
     * @param phoneNumber contact's number
     * @param contactName contact's name
     * @return Builder.build()
     */
    private static Uri buildUri(String phoneNumber, String contactName) {
        phoneNumber = PhoneNumberUtils.normalizeNumber(phoneNumber);
        Uri.Builder builder = Phone.CONTENT_URI.buildUpon();

        if (phoneNumber != null && !phoneNumber.isEmpty()) {
            builder = Uri.withAppendedPath(Phone.CONTENT_FILTER_URI, Uri.encode(phoneNumber)).buildUpon();
            builder.appendQueryParameter(ContactsContract.STREQUENT_PHONE_ONLY, "true");
        }

        if (contactName != null && !contactName.isEmpty()) {
            builder = Uri.withAppendedPath(Phone.CONTENT_FILTER_URI, Uri.encode(contactName)).buildUpon();
            builder.appendQueryParameter(ContactsContract.PRIMARY_ACCOUNT_NAME, "true");
        }

        builder.appendQueryParameter(ContactsContract.REMOVE_DUPLICATE_ENTRIES, "true");
        builder.appendQueryParameter(Contacts.EXTRA_ADDRESS_BOOK_INDEX, "true");
        return builder.build();
    }

}
