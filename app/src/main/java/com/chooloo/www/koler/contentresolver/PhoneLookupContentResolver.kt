package com.chooloo.www.koler.contentresolver

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.ContactsContract.PhoneLookup
import com.chooloo.www.koler.entity.Contact

class PhoneLookupContentResolver(
        context: Context,
        private val number: String,
) : BaseContentResolver<Array<Contact>>(context) {

    override fun onGetUri(): Uri {
        return PhoneLookup.CONTENT_FILTER_URI.buildUpon().appendPath(number).build()
    }

    override fun onGetFilterUri(): Uri {
        return PhoneLookup.CONTENT_FILTER_URI
    }

    override fun onGetProjection() = arrayOf(
            PhoneLookup.CONTACT_ID,
            PhoneLookup.DISPLAY_NAME_PRIMARY,
            PhoneLookup.NUMBER,
            PhoneLookup.PHOTO_URI,
            PhoneLookup.STARRED
    )

    override fun convertCursorToContent(cursor: Cursor?): Array<Contact> = ArrayList<Contact>().apply {
        while (cursor != null && cursor.moveToNext()) cursor.apply {
            add(Contact(
                    contactId = getLong(getColumnIndex(PhoneLookup.CONTACT_ID)),
                    name = getString(getColumnIndex(PhoneLookup.DISPLAY_NAME_PRIMARY)),
                    number = getString(getColumnIndex(PhoneLookup.NUMBER)),
                    photoUri = getString(getColumnIndex(PhoneLookup.PHOTO_URI)),
                    starred = "1" == getString(getColumnIndex(PhoneLookup.STARRED))
            ))
        }
    }.toTypedArray()
}