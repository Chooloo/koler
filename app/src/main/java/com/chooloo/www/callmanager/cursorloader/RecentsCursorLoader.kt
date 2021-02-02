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
package com.chooloo.www.callmanager.cursorloader

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.CallLog
import android.telephony.PhoneNumberUtils
import androidx.loader.content.CursorLoader
import com.chooloo.www.callmanager.entity.Recent
import java.util.*

class RecentsCursorLoader(context: Context?, phoneNumber: String?, contactName: String?) : CursorLoader(
        context!!,
        buildUri(phoneNumber, contactName),
        RECENTS_PROJECTION,
        RECENTS_SELECTION,
        null,
        RECENTS_ORDER) {

    companion object {
        private const val COLUMN_ID = CallLog.Calls._ID
        private const val COLUMN_NUMBER = CallLog.Calls.NUMBER
        private const val COLUMN_PRESENTATION = CallLog.Calls.NUMBER_PRESENTATION
        private const val COLUMN_DATE = CallLog.Calls.DATE
        private const val COLUMN_DURATION = CallLog.Calls.DURATION
        private const val COLUMN_TYPE = CallLog.Calls.TYPE
        private const val COLUMN_CACHED_NAME = CallLog.Calls.CACHED_NAME

        private const val RECENTS_ORDER = "$COLUMN_DATE DESC"
        private val RECENTS_SELECTION: String? = null
        private val RECENTS_PROJECTION = arrayOf(
                COLUMN_ID,
                COLUMN_NUMBER,
                COLUMN_DATE,
                COLUMN_DURATION,
                COLUMN_TYPE,
                COLUMN_CACHED_NAME,
                COLUMN_PRESENTATION
        )

        @JvmStatic
        fun getRecentCallFromCursor(cursor: Cursor?): Recent {
            return try {
                when {
                    cursor == null -> Recent.UNKNOWN
                    else -> Recent(
                            callId = cursor.getLong(cursor.getColumnIndex(COLUMN_ID)),
                            callerNumber = cursor.getString(cursor.getColumnIndex(COLUMN_NUMBER)),
                            callDuration = cursor.getString(cursor.getColumnIndex(COLUMN_DURATION)),
                            callDate = Date(cursor.getLong(cursor.getColumnIndex(COLUMN_DATE))),
                            callType = cursor.getInt(cursor.getColumnIndex(COLUMN_TYPE))
                    )
                }
            } catch (e: IndexOutOfBoundsException) {
                Recent.UNKNOWN
            }
        }

        private fun buildUri(phoneNumber: String?, contactName: String?): Uri {
            val normalNumber = PhoneNumberUtils.normalizeNumber(phoneNumber)
            var uriBuilder = CallLog.Calls.CONTENT_URI.buildUpon()

            if (normalNumber != null && !normalNumber.isEmpty()) {
                uriBuilder = Uri.withAppendedPath(CallLog.Calls.CONTENT_FILTER_URI, Uri.encode(normalNumber)).buildUpon()
            }

            if (contactName != null && !contactName.isEmpty()) {
                uriBuilder = Uri.withAppendedPath(CallLog.Calls.CONTENT_FILTER_URI, Uri.encode(contactName)).buildUpon()
            }
            return uriBuilder.build()
        }
    }
}