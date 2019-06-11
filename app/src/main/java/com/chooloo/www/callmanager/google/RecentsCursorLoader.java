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
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;

import androidx.loader.content.CursorLoader;

public final class RecentsCursorLoader extends CursorLoader {

    public static String CURSOR_NAME_COLUMN = Phone.DISPLAY_NAME_PRIMARY;
    public static String CURSOR_NUMBER_COLUMN = Phone.NUMBER;

    /**
     * Cursor selection string
     */
    public static String[] RECENTS_PROJECTION_DISPLAY_NAME_PRIMARY =
            new String[]{
                    CallLog.Calls._ID,
                    CallLog.Calls.NUMBER,
                    CallLog.Calls.DATE,
                    CallLog.Calls.DURATION,
                    CallLog.Calls.TYPE,
                    CallLog.Calls.CACHED_NAME
            };

    private static String RECENTS_ORDER = CallLog.Calls.DATE + " DESC";

    /**
     * Constructor
     *
     * @param context
     * @param phoneNumber String
     * @param contactName String
     */
    public RecentsCursorLoader(Context context, String phoneNumber, String contactName) {
        super(
                context,
                buildUri(phoneNumber, contactName),
                RECENTS_PROJECTION_DISPLAY_NAME_PRIMARY,
                getSelection(contactName, phoneNumber),
                null,
                RECENTS_ORDER);
    }

    private static String getSelection(String contactName, String phoneNumber) {
        if (contactName != null && !contactName.isEmpty())
            return CallLog.Calls.CACHED_NAME + " LIKE '%" + contactName + "%'";
        else if (phoneNumber != null && !phoneNumber.isEmpty())
            return CallLog.Calls.NUMBER + " LIKE '%" + phoneNumber + "%'";
        else return null;
    }

    /**
     * Builds contact uri by given name and phone number
     *
     * @param phoneNumber
     * @param contactName
     * @return Builder.build()
     */
    private static Uri buildUri(String phoneNumber, String contactName) {
        Uri.Builder builder;
        if (phoneNumber != null && !phoneNumber.isEmpty()) {
            builder = Uri.withAppendedPath(CallLog.Calls.CONTENT_FILTER_URI, Uri.encode(phoneNumber)).buildUpon();
            builder.appendQueryParameter(ContactsContract.STREQUENT_PHONE_ONLY, "true");
        } else {
            builder = CallLog.Calls.CONTENT_URI.buildUpon();
        }

        return builder.build();
    }

}
