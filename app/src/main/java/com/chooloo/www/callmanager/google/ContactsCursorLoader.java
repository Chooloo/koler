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

package com.chooloo.www.callmanager.google;

import android.content.Context;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;

import androidx.loader.content.CursorLoader;

import static android.provider.ContactsContract.Contacts;

public final class ContactsCursorLoader extends CursorLoader {

    public static final int CONTACT_ID = 0;
    public static final int CONTACT_DISPLAY_NAME = 1;
    public static final int CONTACT_PHOTO_ID = 2;
    public static final int CONTACT_PHOTO_URI = 3;
    public static final int CONTACT_NUMBER = 4;

    public static final String[] CONTACTS_PROJECTION_DISPLAY_NAME_PRIMARY =
            new String[]{
                    Phone._ID, // 0
                    Phone.DISPLAY_NAME_PRIMARY, // 1
                    Phone.PHOTO_ID, // 2
                    Phone.PHOTO_THUMBNAIL_URI, // 3
                    Phone.NUMBER, // 4
            };

    public ContactsCursorLoader(Context context, String phoneNumber, String contactName) {
        super(
                context,
                buildUri(phoneNumber, contactName),
                getProjection(context),
                getWhere(context),
                null,
                getSortKey(context) + " ASC");
    }

    private static String[] getProjection(Context context) {
        return CONTACTS_PROJECTION_DISPLAY_NAME_PRIMARY;
    }

    private static String getWhere(Context context) {
        String where = getProjection(context)[CONTACT_DISPLAY_NAME] + " IS NOT NULL" +
                " AND " + Contacts.HAS_PHONE_NUMBER + "=1" +
                " AND " + ContactsContract.RawContacts.ACCOUNT_TYPE + " NOT LIKE '%whatsapp%'" +
                " AND " + ContactsContract.RawContacts.ACCOUNT_TYPE + " NOT LIKE '%tachyon%'";

        return where;
    }

    private static String getSortKey(Context context) {
        return Contacts.SORT_KEY_PRIMARY;
    }

    private static Uri buildUri(String phoneNumber, String contactName) {
        Uri.Builder builder;
        if (phoneNumber != null && !phoneNumber.isEmpty()) {
            builder = Uri.withAppendedPath(Phone.CONTENT_FILTER_URI, Uri.encode(phoneNumber)).buildUpon();
            builder.appendQueryParameter(ContactsContract.STREQUENT_PHONE_ONLY, "true");
        } else if (contactName != null && !contactName.isEmpty()) {
            builder = Uri.withAppendedPath(Phone.CONTENT_FILTER_URI, Uri.encode(contactName)).buildUpon();
            builder.appendQueryParameter(ContactsContract.PRIMARY_ACCOUNT_NAME, "true");
        } else {
            builder = Phone.CONTENT_URI.buildUpon();
        }

        builder.appendQueryParameter(Contacts.EXTRA_ADDRESS_BOOK_INDEX, "true");
        builder.appendQueryParameter(ContactsContract.REMOVE_DUPLICATE_ENTRIES, "true");
        return builder.build();
    }
}
