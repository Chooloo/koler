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

import com.chooloo.www.callmanager.database.entity.Contact;

import static android.provider.ContactsContract.Contacts;

public class NewContactsCursorLoader extends CursorLoader {

    // Columns
    public static String COLUMN_ID = Contacts._ID;
    public static String COLUMN_NAME = Contacts.DISPLAY_NAME_PRIMARY;
    public static String COLUMN_THUMBNAIL = Contacts.PHOTO_THUMBNAIL_URI;
    public static String COLUMN_NUMBER = Phone.NORMALIZED_NUMBER;
    public static String COLUMN_STARRED = Phone.STARRED;

    private static String CONTACTS_ORDER = Contacts.SORT_KEY_PRIMARY + " ASC";

    /**
     * Cursor selection string
     * Columns to load
     */
    private static final String[] CONTACTS_PROJECTION =
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
     * @param context     calling context
     * @param phoneNumber String
     * @param contactName String
     */
    public NewContactsCursorLoader(Context context, String phoneNumber, String contactName) {
        super(
                context,
                buildUri(),
                CONTACTS_PROJECTION,
                getSelection(phoneNumber, contactName),
                null,
                CONTACTS_ORDER);
    }

    /**
     * Return a selection query for the cursor
     * According to given name and phone number
     * By default they're set to "", if they're not null, than they'le be set to their value
     *
     * @return String sql style
     */
    private static String getSelection(String phoneNumber, String contactName) {
        phoneNumber = PhoneNumberUtils.normalizeNumber(phoneNumber);
        if (phoneNumber == null) phoneNumber = "";
        if (contactName == null) contactName = "";
        return "((" + COLUMN_NAME + " IS NOT NULL" +
                " AND " + COLUMN_NAME + " LIKE '%" + contactName + "%')" +
                " OR (" + Phone.DISPLAY_NAME_ALTERNATIVE + " IS NOT NULL" +
                " AND " + Phone.DISPLAY_NAME_ALTERNATIVE + " LIKE '%" + contactName + "%'))" +
                " AND " + COLUMN_NUMBER + " LIKE '%" + phoneNumber + "%'" +
                " AND " + Contacts.HAS_PHONE_NUMBER + "=1" +
                " AND (" + ContactsContract.RawContacts.ACCOUNT_NAME + " IS NULL" +
                " OR ( " + ContactsContract.RawContacts.ACCOUNT_TYPE + " NOT LIKE '%whatsapp%'" +
                " AND " + ContactsContract.RawContacts.ACCOUNT_TYPE + " NOT LIKE '%tachyon%'" + "))";
    }

    /**
     * Builds contact uri with appropriate queries parameters
     *
     * @return Builder.build()
     */
    private static Uri buildUri() {
        Uri.Builder builder = Phone.CONTENT_URI.buildUpon();
        builder.appendQueryParameter(ContactsContract.STREQUENT_PHONE_ONLY, "true");
        builder.appendQueryParameter(ContactsContract.PRIMARY_ACCOUNT_NAME, "true");
        builder.appendQueryParameter(ContactsContract.REMOVE_DUPLICATE_ENTRIES, "true");
        builder.appendQueryParameter(Contacts.EXTRA_ADDRESS_BOOK_INDEX, "true");
        return builder.build();
    }

}
