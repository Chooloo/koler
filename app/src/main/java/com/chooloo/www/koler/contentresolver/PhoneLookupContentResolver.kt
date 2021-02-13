package com.chooloo.www.koler.contentresolver

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.ContactsContract
import android.provider.ContactsContract.PhoneLookup
import com.chooloo.www.koler.entity.Contact

class PhoneLookupContentResolver(
        context: Context,
        private val number: String,
) : BaseContentResolver<Array<Contact>>(context) {

    override fun onGetUri(): Uri {
        return PhoneLookup.CONTENT_FILTER_URI.buildUpon().appendPath(number).build()
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
                    contactId = getLong(getColumnIndex(ContactsContract.Contacts._ID)),
                    name = getString(getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY)),
                    photoUri = getString(getColumnIndex(ContactsContract.Contacts.PHOTO_THUMBNAIL_URI)),
                    starred = "1" == getString(getColumnIndex(ContactsContract.Contacts.STARRED)),
                    lookupKey = getString(getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY))
            ))
        }
    }.toTypedArray()
}