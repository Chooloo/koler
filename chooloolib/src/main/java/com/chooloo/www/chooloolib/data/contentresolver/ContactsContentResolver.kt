package com.chooloo.www.chooloolib.data.contentresolver

import android.content.ContentResolver
import android.database.Cursor
import android.net.Uri
import android.provider.ContactsContract
import com.chooloo.www.chooloolib.domain.model.record.ContactRecord
import com.chooloo.www.chooloolib.utils.SelectionBuilder
import com.chooloo.www.chooloolib.utils.getInt
import com.chooloo.www.chooloolib.utils.getLong
import com.chooloo.www.chooloolib.utils.getString

class ContactsContentResolver(
    filter: String? = null,
    contactId: Long? = null,
    contentResolver: ContentResolver
) : BaseContentResolver<ContactRecord>(filter, contentResolver) {

    private val selectionBuilder = SelectionBuilder()
        .addNotNull(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY)
        .addSelection(ContactsContract.Contacts._ID, contactId)

    override val uri: Uri = ContactsContract.Contacts.CONTENT_URI
    override val filterUri: Uri = ContactsContract.Contacts.CONTENT_FILTER_URI
    override val sortOrder: String = "${ContactsContract.Contacts.SORT_KEY_PRIMARY} ASC"
    override val selection: String? = selectionBuilder.build()
    override val selectionArgs: Array<String>? = selectionBuilder.buildArgs()
    override val projection: Array<String> = arrayOf(
        ContactsContract.Contacts._ID,
        ContactsContract.Contacts.STARRED,
        ContactsContract.Contacts.PHOTO_URI,
        ContactsContract.Contacts.LOOKUP_KEY,
        ContactsContract.Contacts.HAS_PHONE_NUMBER,
        ContactsContract.Contacts.DISPLAY_NAME_PRIMARY,
        ContactsContract.Contacts.PHOTO_THUMBNAIL_URI,
    )

    override fun convertCursorToItem(cursor: Cursor) = ContactRecord(
        id = cursor.getLong(ContactsContract.Contacts._ID),
        lookupKey = cursor.getString(ContactsContract.Contacts.LOOKUP_KEY),
        starred = cursor.getInt(ContactsContract.Contacts.STARRED) == 1,
        hasPhoneNumber = cursor.getInt(ContactsContract.Contacts.HAS_PHONE_NUMBER) > 0,
        name = cursor.getString(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY),
        photoUri = cursor.getString(ContactsContract.Contacts.PHOTO_URI)
            ?: cursor.getString(ContactsContract.Contacts.PHOTO_THUMBNAIL_URI)
    )
}
