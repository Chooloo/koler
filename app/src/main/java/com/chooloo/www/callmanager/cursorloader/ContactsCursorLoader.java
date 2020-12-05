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
import android.telephony.PhoneNumberUtils;

import androidx.annotation.Nullable;
import androidx.loader.content.CursorLoader;


public class ContactsCursorLoader extends CursorLoader {

    public static String COLUMN_ID = ContactsContract.Contacts._ID;
    public static String COLUMN_NAME = ContactsContract.Contacts.DISPLAY_NAME_PRIMARY;
    public static String COLUMN_THUMBNAIL = ContactsContract.Contacts.PHOTO_THUMBNAIL_URI;
    public static String COLUMN_STARRED = ContactsContract.Contacts.STARRED;

    protected static final String CONTACTS_ORDER = ContactsContract.Contacts.SORT_KEY_PRIMARY + " ASC";
    protected static final String CONTACTS_SELECTION = ContactsContract.Contacts.DISPLAY_NAME_PRIMARY + " IS NOT NULL";
    protected static final String[] CONTACTS_PROJECTION =
            new String[]{
                    COLUMN_ID,
                    COLUMN_NAME,
                    COLUMN_THUMBNAIL,
                    COLUMN_STARRED
            };

    public ContactsCursorLoader(Context context, @Nullable String phoneNumber, @Nullable String contactName) {
        super(
                context,
                buildUri(phoneNumber, contactName),
                CONTACTS_PROJECTION,
                CONTACTS_SELECTION,
                null,
                CONTACTS_ORDER);
    }

    protected static Uri buildUri(@Nullable String phoneNumber, @Nullable String contactName) {
        phoneNumber = PhoneNumberUtils.normalizeNumber(phoneNumber);
        Uri.Builder uriBuilder = ContactsContract.Contacts.CONTENT_URI.buildUpon();

        if (phoneNumber != null && !phoneNumber.isEmpty()) {
            uriBuilder = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_FILTER_URI, Uri.encode(phoneNumber)).buildUpon();
            uriBuilder.appendQueryParameter(ContactsContract.STREQUENT_PHONE_ONLY, "true");
        }

        if (contactName != null && !contactName.isEmpty()) {
            uriBuilder = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_FILTER_URI, Uri.encode(contactName)).buildUpon();
        }

        uriBuilder.appendQueryParameter(ContactsContract.REMOVE_DUPLICATE_ENTRIES, "true");
        uriBuilder.appendQueryParameter(ContactsContract.Contacts.EXTRA_ADDRESS_BOOK_INDEX, "true");
        uriBuilder.appendQueryParameter(ContactsContract.Contacts.EXTRA_ADDRESS_BOOK_INDEX_COUNTS, "true");
        uriBuilder.appendQueryParameter(ContactsContract.Contacts.EXTRA_ADDRESS_BOOK_INDEX_TITLES, "true");
        return uriBuilder.build();
    }

}
