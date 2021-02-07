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
package com.chooloo.www.koler.cursorloader

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.ContactsContract
import android.telephony.PhoneNumberUtils
import androidx.loader.content.CursorLoader
import com.chooloo.www.koler.entity.Contact

open class ContactsCursorLoader(context: Context, phoneNumber: String?, contactName: String?) : CursorLoader(
        context,
        buildUri(phoneNumber, contactName),
        CONTACTS_PROJECTION,
        CONTACTS_SELECTION,
        null,
        CONTACTS_ORDER
) {
    companion object {
        const val EXTRA_INDEX_COUNTS = ContactsContract.Contacts.EXTRA_ADDRESS_BOOK_INDEX_COUNTS
        const val EXTRA_INDEX_TITLES = ContactsContract.Contacts.EXTRA_ADDRESS_BOOK_INDEX_TITLES

        const val COLUMN_ID = ContactsContract.Contacts._ID
        const val COLUMN_NAME = ContactsContract.Contacts.DISPLAY_NAME_PRIMARY
        const val COLUMN_THUMBNAIL = ContactsContract.Contacts.PHOTO_THUMBNAIL_URI
        const val COLUMN_STARRED = ContactsContract.Contacts.STARRED

        const val CONTACTS_ORDER = ContactsContract.Contacts.SORT_KEY_PRIMARY + " ASC"
        const val CONTACTS_SELECTION = ContactsContract.Contacts.DISPLAY_NAME_PRIMARY + " IS NOT NULL"
        val CONTACTS_PROJECTION = arrayOf(
                COLUMN_ID,
                COLUMN_NAME,
                COLUMN_THUMBNAIL,
                COLUMN_STARRED
        )

        @JvmStatic
        fun getContactFromCursor(cursor: Cursor?): Contact {
            return try {
                when {
                    cursor == null -> Contact.UNKNOWN
                    else -> Contact(contactId = cursor.getLong(cursor.getColumnIndex(COLUMN_ID)),
                            name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME)),
                            photoUri = cursor.getString(cursor.getColumnIndex(COLUMN_THUMBNAIL)),
                            starred = "1" == cursor.getString(cursor.getColumnIndex(COLUMN_STARRED)))
                }
            } catch (e: IndexOutOfBoundsException) {
                Contact.UNKNOWN
            }
        }

        @JvmStatic
        protected fun buildUri(phoneNumber: String?, contactName: String?): Uri {
            val normalNumber = PhoneNumberUtils.normalizeNumber(phoneNumber)
            var uriBuilder = ContactsContract.Contacts.CONTENT_URI.buildUpon()

            if (phoneNumber != null && !normalNumber.isEmpty()) {
                uriBuilder = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_FILTER_URI, Uri.encode(phoneNumber)).buildUpon()
                uriBuilder.appendQueryParameter(ContactsContract.STREQUENT_PHONE_ONLY, "true")
            }

            if (contactName != null && !contactName.isEmpty()) {
                uriBuilder = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_FILTER_URI, Uri.encode(contactName)).buildUpon()
            }

            uriBuilder.appendQueryParameter(ContactsContract.REMOVE_DUPLICATE_ENTRIES, "true")
            uriBuilder.appendQueryParameter(ContactsContract.Contacts.EXTRA_ADDRESS_BOOK_INDEX, "true")
            return uriBuilder.build()
        }
    }
}