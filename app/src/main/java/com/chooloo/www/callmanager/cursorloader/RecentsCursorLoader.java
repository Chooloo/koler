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
import android.provider.CallLog.Calls;
import android.provider.ContactsContract;
import android.telecom.Call;
import android.telephony.PhoneNumberUtils;

import androidx.annotation.Nullable;
import androidx.loader.content.CursorLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class RecentsCursorLoader extends CursorLoader {

    public static final String COLUMN_ID = Calls._ID;
    public static final String COLUMN_NUMBER = Calls.NUMBER;
    public static final String COLUMN_PRESENTATION = Calls.NUMBER_PRESENTATION;
    public static final String COLUMN_DATE = Calls.DATE;
    public static final String COLUMN_DURATION = Calls.DURATION;
    public static final String COLUMN_TYPE = Calls.TYPE;
    public static final String COLUMN_CACHED_NAME = Calls.CACHED_NAME;

    private static final String RECENTS_ORDER = COLUMN_DATE + " DESC";
    private static final String RECENTS_SELECTION = null;
    private static final String[] RECENTS_PROJECTION =
            new String[]{
                    COLUMN_ID,
                    COLUMN_NUMBER,
                    COLUMN_DATE,
                    COLUMN_DURATION,
                    COLUMN_TYPE,
                    COLUMN_CACHED_NAME,
                    COLUMN_PRESENTATION
            };

    public RecentsCursorLoader(Context context, @Nullable String phoneNumber, @Nullable String contactName) {
        super(
                context,
                buildUri(phoneNumber, contactName),
                RECENTS_PROJECTION,
                RECENTS_SELECTION,
                null,
                RECENTS_ORDER);
    }

    private static Uri buildUri(@Nullable String phoneNumber, @Nullable String contactName) {
        phoneNumber = PhoneNumberUtils.normalizeNumber(phoneNumber);
        Uri.Builder uriBuilder = Calls.CONTENT_URI.buildUpon();

        if (phoneNumber != null && !phoneNumber.isEmpty()) {
            uriBuilder = Uri.withAppendedPath(Calls.CONTENT_FILTER_URI, Uri.encode(phoneNumber)).buildUpon();
        }

        if (contactName != null && !contactName.isEmpty()) {
            uriBuilder = Uri.withAppendedPath(Calls.CONTENT_FILTER_URI, Uri.encode(contactName)).buildUpon();
        }

        return uriBuilder.build();
    }

}
