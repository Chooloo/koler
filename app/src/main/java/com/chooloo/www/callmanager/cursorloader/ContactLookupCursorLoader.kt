package com.chooloo.www.callmanager.cursorloader

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.ContactsContract
import android.provider.ContactsContract.PhoneLookup
import android.telephony.PhoneNumberUtils
import androidx.loader.content.CursorLoader
import com.chooloo.www.callmanager.entity.Contact

class ContactLookupCursorLoader(context: Context, phoneNumber: String) : CursorLoader(
        context,
        buildUri(phoneNumber),
        CONTACT_LOOKUP_PROJECTION,
        CONTACT_LOOKUP_SELECTION,
        null,
        CONTACT_LOOKUP_ORDER
) {

    companion object {
        const val COLUMN_ID = PhoneLookup.CONTACT_ID
        const val COLUMN_NAME = PhoneLookup.DISPLAY_NAME_PRIMARY
        const val COLUMN_NUMBER = PhoneLookup.NUMBER
        const val CONTACT_LOOKUP_ORDER = PhoneLookup.DISPLAY_NAME_PRIMARY + " ASC"
        const val CONTACT_LOOKUP_SELECTION = ContactsContract.Contacts.DISPLAY_NAME_PRIMARY + " IS NOT NULL"
        val CONTACT_LOOKUP_PROJECTION = arrayOf(COLUMN_ID, COLUMN_NAME, COLUMN_NUMBER)

        @JvmStatic
        fun lookupContact(context: Context, phoneNumber: String): Contact {
            return ContactLookupCursorLoader(context, phoneNumber).loadContact()
        }

        fun buildUri(phoneNumber: String): Uri {
            val builder = PhoneLookup.CONTENT_FILTER_URI.buildUpon()
            builder.appendPath(PhoneNumberUtils.normalizeNumber(phoneNumber))
            builder.appendQueryParameter(ContactsContract.STREQUENT_PHONE_ONLY, "true")
            builder.appendQueryParameter(ContactsContract.REMOVE_DUPLICATE_ENTRIES, "true")
            return builder.build()
        }
    }

    fun loadContact(): Contact {
        val cursor: Cursor? = super.loadInBackground()
        if (cursor == null || cursor.count == 0) {
            return Contact.UNKNOWN
        }
        cursor.moveToFirst()
        val name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME))
        val number = cursor.getString(cursor.getColumnIndex(COLUMN_NUMBER))
        val id = cursor.getLong(cursor.getColumnIndex(COLUMN_ID))
        cursor.close()
        return Contact(name = name, number = number, contactId = id)
    }
}
