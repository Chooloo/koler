package com.chooloo.www.chooloolib.contentresolver

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.ContactsContract
import com.chooloo.www.chooloolib.model.ContactAccount
import com.chooloo.www.chooloolib.util.SelectionBuilder

class ContactsContentResolver(context: Context, contactId: Long? = null) :
    BaseContentResolver<ContactAccount>(context) {

    override val selectionArgs: Array<String>? = null
    override val uri: Uri = ContactsContract.Contacts.CONTENT_URI
    override val filterUri: Uri = ContactsContract.Contacts.CONTENT_FILTER_URI
    override val sortOrder: String = "${ContactsContract.Contacts.SORT_KEY_PRIMARY} ASC"
    override val projection: Array<String> = arrayOf(
        ContactsContract.Contacts._ID,
        ContactsContract.Contacts.STARRED,
        ContactsContract.Contacts.LOOKUP_KEY,
        ContactsContract.Contacts.DISPLAY_NAME_PRIMARY,
        ContactsContract.Contacts.PHOTO_THUMBNAIL_URI,
    )
    override val selection by lazy {
        SelectionBuilder()
            .addNotNull(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY)
            .addSelection(ContactsContract.Contacts._ID, contactId)
            .build()
    }

    @SuppressLint("Range")
    override fun convertCursorToItem(cursor: Cursor) = ContactAccount(
        id = cursor.getLong(cursor.getColumnIndex(ContactsContract.Contacts._ID)),
        lookupKey = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY)),
        starred = "1" == cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.STARRED)),
        name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY)),
        photoUri = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.PHOTO_THUMBNAIL_URI))
    )
}