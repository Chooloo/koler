package com.chooloo.www.chooloolib.data.contentresolver

import android.content.ContentResolver
import android.database.Cursor
import android.net.Uri
import android.provider.ContactsContract.PhoneLookup
import com.chooloo.www.chooloolib.domain.model.record.PhoneLookupRecord
import com.chooloo.www.chooloolib.utils.getInt
import com.chooloo.www.chooloolib.utils.getLong
import com.chooloo.www.chooloolib.utils.getString

class PhoneLookupContentResolver(
    number: String?,
    filter: String? = null,
    contentResolver: ContentResolver
) : BaseContentResolver<PhoneLookupRecord>(filter, contentResolver) {

    override val selection: String? = null
    override val sortOrder: String? = null
    override val selectionArgs: Array<String>? = null
    override val filterUri: Uri = PhoneLookup.CONTENT_FILTER_URI
    override val uri: Uri = Uri.withAppendedPath(
        PhoneLookup.CONTENT_FILTER_URI,
        Uri.encode(number.orEmpty())
    )
    override val projection: Array<String> = arrayOf(
        PhoneLookup.TYPE,
        PhoneLookup.LABEL,
        PhoneLookup.NUMBER,
        PhoneLookup.STARRED,
        PhoneLookup.PHOTO_URI,
        PhoneLookup.CONTACT_ID,
        PhoneLookup.DISPLAY_NAME,
    )

    override fun convertCursorToItem(cursor: Cursor) = PhoneLookupRecord(
        label = cursor.getString(PhoneLookup.LABEL),
        number = cursor.getString(PhoneLookup.NUMBER),
        name = cursor.getString(PhoneLookup.DISPLAY_NAME),
        contactId = cursor.getLong(PhoneLookup.CONTACT_ID),
        photoUri = cursor.getString(PhoneLookup.PHOTO_URI),
        starred = cursor.getInt(PhoneLookup.STARRED) == 1,
        type = cursor.getInt(PhoneLookup.TYPE)
    )
}
