package com.chooloo.www.koler.contentresolver

import android.Manifest
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.ContactsContract
import com.chooloo.www.koler.data.account.Contact
import com.chooloo.www.koler.util.SelectionBuilder

class ContactsContentResolver(context: Context, contactId: Long? = null) :
    BaseItemsContentResolver<Contact>(context) {

    override val uri: Uri = URI
    override val filterUri: Uri = FILTER_URI
    override val sortOrder: String = SORT_ORDER
    override val selectionArgs: Array<String>? = null
    override val projection: Array<String> = PROJECTION
    override val selection by lazy {
        SelectionBuilder()
            .addNotNull(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY)
            .addSelection(ContactsContract.Contacts._ID, contactId)
            .build()
    }

    override fun convertCursorToItem(cursor: Cursor) =
        Contact(
            id = cursor.getLong(cursor.getColumnIndex(ContactsContract.Contacts._ID)),
            lookupKey = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY)),
            starred = "1" == cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.STARRED)),
            name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY)),
            photoUri = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.PHOTO_THUMBNAIL_URI))
        )

    companion object {
        val URI: Uri = ContactsContract.Contacts.CONTENT_URI
        val FILTER_URI: Uri = ContactsContract.Contacts.CONTENT_FILTER_URI
        val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.READ_CONTACTS)
        val PROJECTION: Array<String> = arrayOf(
            ContactsContract.Contacts._ID,
            ContactsContract.Contacts.STARRED,
            ContactsContract.Contacts.LOOKUP_KEY,
            ContactsContract.Contacts.DISPLAY_NAME_PRIMARY,
            ContactsContract.Contacts.PHOTO_THUMBNAIL_URI,
        )
        const val SORT_ORDER = "${ContactsContract.Contacts.SORT_KEY_PRIMARY} ASC"
    }
}