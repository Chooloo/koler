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
import android.provider.CallLog.Calls;

import androidx.loader.content.CursorLoader;

import org.apache.commons.codec.binary.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import timber.log.Timber;

public final class RecentsCursorLoader extends CursorLoader {

    // Columns
    public static String COLUMN_ID = Calls._ID;
    public static String COLUMN_NUMBER = Calls.NUMBER;
    public static String COLUMN_DATE = Calls.DATE;
    public static String COLUMN_DURATION = Calls.DURATION;
    public static String COLUMN_TYPE = Calls.TYPE;
    public static String COLUMN_CACHED_NAME = Calls.CACHED_NAME;

    private static String RECENTS_ORDER = COLUMN_DATE + " DESC";

    /**
     * Cursor selection string
     * Column to load
     */
    private static String[] RECENTS_PROJECTION =
            new String[]{
                    COLUMN_ID,
                    COLUMN_NUMBER,
                    COLUMN_DATE,
                    COLUMN_DURATION,
                    COLUMN_TYPE,
                    COLUMN_CACHED_NAME
            };

    /**
     * Constructor
     *
     * @param context     calling context
     * @param phoneNumber String
     * @param contactName String
     */
    public RecentsCursorLoader(Context context, String phoneNumber, String contactName) {
        super(
                context,
                Calls.CONTENT_URI.buildUpon().build(),
                RECENTS_PROJECTION,
                getSelection(contactName, phoneNumber),
                null,
                RECENTS_ORDER);
    }

    /**
     * Return a selection query for the cursor
     * According to given name and phone number
     * By default they're set to "", if they're not null, than they'le be set to their value
     *
     * @param contactName name of the contact duh
     * @param phoneNumber duhh
     * @return String sql style string
     */
    private static String getSelection(String contactName, String phoneNumber) {
        List<String> conditions = new ArrayList<String>();

        if (contactName != null)
            conditions.add(COLUMN_CACHED_NAME + " LIKE '%" + contactName + "%'");

        if (phoneNumber != null)
            conditions.add(COLUMN_NUMBER + " LIKE '%" + phoneNumber + "%'");

        if (conditions.size() == 0) return "";
        else return conditions.stream().collect(Collectors.joining(" AND "));
    }

}
