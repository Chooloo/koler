package com.chooloo.www.chooloolib.contentresolver

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.ContactsContract.PhoneLookup
import com.chooloo.www.chooloolib.model.PhoneLookupAccount

class PhoneLookupContentResolver(context: Context, number: String?) :
    BaseContentResolver<PhoneLookupAccount>(context) {

    override val selection: String? = null
    override val sortOrder: String? = null
    override val selectionArgs: Array<String>? = null
    override val filterUri: Uri = PhoneLookup.CONTENT_FILTER_URI
    override val uri: Uri = PhoneLookup.CONTENT_FILTER_URI.buildUpon().appendPath(number).build()
    override val projection: Array<String> = arrayOf(
        PhoneLookup.TYPE,
        PhoneLookup.LABEL,
        PhoneLookup.NUMBER,
        PhoneLookup.STARRED,
        PhoneLookup.PHOTO_URI,
        PhoneLookup.CONTACT_ID,
        PhoneLookup.DISPLAY_NAME,
    )

    @SuppressLint("Range")
    override fun convertCursorToItem(cursor: Cursor) = PhoneLookupAccount(
        label = cursor.getString(cursor.getColumnIndex(PhoneLookup.LABEL)),
        number = cursor.getString(cursor.getColumnIndex(PhoneLookup.NUMBER)),
        name = cursor.getString(cursor.getColumnIndex(PhoneLookup.DISPLAY_NAME)),
        contactId = cursor.getLong(cursor.getColumnIndex(PhoneLookup.CONTACT_ID)),
        photoUri = cursor.getString(cursor.getColumnIndex(PhoneLookup.PHOTO_URI)),
        starred = "1" == cursor.getString(cursor.getColumnIndex(PhoneLookup.STARRED)),
        type = cursor.getInt(cursor.getColumnIndex(PhoneLookup.TYPE))
    )
}