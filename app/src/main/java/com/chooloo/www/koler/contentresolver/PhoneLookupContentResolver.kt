package com.chooloo.www.koler.contentresolver

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.ContactsContract.PhoneLookup
import com.chooloo.www.koler.data.PhoneLookupAccount

class PhoneLookupContentResolver(context: Context, number: String?) :
    BaseItemsContentResolver<PhoneLookupAccount>(context) {

    override val filterUri: Uri = URI
    override val selection: String? = null
    override val sortOrder: String? = null
    override val selectionArgs: Array<String>? = null
    override val projection: Array<String> = PROJECTION
    override val uri: Uri = URI.buildUpon().appendPath(number).build()

    override fun convertCursorToItem(cursor: Cursor): PhoneLookupAccount {
        return PhoneLookupAccount(
            number = cursor.getString(cursor.getColumnIndex(PhoneLookup.NUMBER)),
            name = cursor.getString(cursor.getColumnIndex(PhoneLookup.DISPLAY_NAME)),
            contactId = cursor.getLong(cursor.getColumnIndex(PhoneLookup.CONTACT_ID)),
            photoUri = cursor.getString(cursor.getColumnIndex(PhoneLookup.PHOTO_URI)),
            starred = "1" == cursor.getString(cursor.getColumnIndex(PhoneLookup.STARRED)),
            type = cursor.getInt(cursor.getColumnIndex(PhoneLookup.TYPE))
        )
    }

    companion object {
        val URI: Uri = PhoneLookup.CONTENT_FILTER_URI
        val PROJECTION: Array<String> = arrayOf(
            PhoneLookup.TYPE,
            PhoneLookup.NUMBER,
            PhoneLookup.STARRED,
            PhoneLookup.PHOTO_URI,
            PhoneLookup.CONTACT_ID,
            PhoneLookup.DISPLAY_NAME,
        )
    }
}