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
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog.Calls;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.util.Log;

import androidx.loader.content.CursorLoader;

import timber.log.Timber;

public final class RecentsCursorLoader extends CursorLoader {

    public static String CURSOR_NAME_COLUMN = Phone.DISPLAY_NAME_PRIMARY;
    public static String CURSOR_NUMBER_COLUMN = Phone.NUMBER;

    public static String COLUMN_ID = Calls._ID;
    public static String COLUMN_NUMBER = Calls.NUMBER;
    public static String COLUMN_DATE = Calls.DATE;
    public static String COLUMN_DURATION = Calls.DURATION;
    public static String COLUMN_TYPE = Calls.TYPE;
    public static String COLUMN_CACHED_NAME = Calls.CACHED_NAME;

    /**
     * Cursor selection string
     */
    private static String[] RECENTS_PROJECTION_DISPLAY_NAME_PRIMARY =
            new String[]{
                    COLUMN_ID,
                    COLUMN_NUMBER,
                    COLUMN_DATE,
                    COLUMN_DURATION,
                    COLUMN_TYPE,
                    COLUMN_CACHED_NAME
            };

    private static String RECENTS_ORDER = COLUMN_DATE + " DESC";

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
                Calls.CONTENT_URI.buildUpon().build(),
                RECENTS_PROJECTION_DISPLAY_NAME_PRIMARY,
                getSelection(contactName, phoneNumber),
                null,
                RECENTS_ORDER);
    }

    private static String getSelection(String contactName, String phoneNumber) {
        if (contactName == null) contactName = "";
        if (phoneNumber == null) phoneNumber = "";
        return COLUMN_CACHED_NAME + " LIKE '%" + contactName + "%' AND " +
                COLUMN_NUMBER + " LIKE '%" + phoneNumber + "%'";
    }

}
