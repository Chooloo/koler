package com.chooloo.www.chooloolib.data.contentresolver

import android.content.ContentResolver
import android.database.Cursor
import android.net.Uri
import android.provider.ContactsContract.CommonDataKinds.Phone
import com.chooloo.www.chooloolib.domain.model.record.PhoneRecord
import com.chooloo.www.chooloolib.utils.SelectionBuilder
import com.chooloo.www.chooloolib.utils.getInt
import com.chooloo.www.chooloolib.utils.getLong
import com.chooloo.www.chooloolib.utils.getString

class PhonesContentResolver(
    filter: String? = null,
    contactId: Long? = null,
    contentResolver: ContentResolver
) : BaseContentResolver<PhoneRecord>(filter, contentResolver) {

    private val selectionBuilder = SelectionBuilder().addSelection(Phone.CONTACT_ID, contactId)

    override val uri: Uri = Phone.CONTENT_URI
    override val filterUri: Uri = Phone.CONTENT_FILTER_URI
    override val sortOrder: String = "${Phone.IS_SUPER_PRIMARY} DESC, ${Phone.IS_PRIMARY} DESC"
    override val selection: String? = selectionBuilder.build()
    override val selectionArgs: Array<String>? = selectionBuilder.buildArgs()
    override val projection: Array<String> = arrayOf(
        Phone._ID,
        Phone.TYPE,
        Phone.LABEL,
        Phone.NUMBER,
        Phone.STARRED,
        Phone.CONTACT_ID,
        Phone.IS_SUPER_PRIMARY,
        Phone.NORMALIZED_NUMBER,
        Phone.PHOTO_THUMBNAIL_URI,
        Phone.DISPLAY_NAME_PRIMARY
    )

    override fun convertCursorToItem(cursor: Cursor) = PhoneRecord(
        id = cursor.getLong(Phone._ID),
        type = cursor.getInt(Phone.TYPE),
        label = cursor.getString(Phone.LABEL),
        number = cursor.getString(Phone.NUMBER) ?: "",
        contactId = cursor.getLong(Phone.CONTACT_ID),
        starred = cursor.getInt(Phone.STARRED) == 1,
        isPrimary = cursor.getInt(Phone.IS_SUPER_PRIMARY) != 0,
        photoUri = cursor.getString(Phone.PHOTO_THUMBNAIL_URI),
        displayName = cursor.getString(Phone.DISPLAY_NAME_PRIMARY) ?: "",
        normalizedNumber = cursor.getString(Phone.NORMALIZED_NUMBER)
    )
}
