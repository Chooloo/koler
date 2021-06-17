package com.chooloo.www.koler.contentresolver

import android.Manifest.permission.READ_CONTACTS
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.ContactsContract.Contacts
import com.chooloo.www.koler.data.Contact

open class ContactsContentResolver(
    context: Context,
    contactId: Long? = null
) : BaseItemsContentResolver<Contact>(context) {
    companion object {
        val URI: Uri = Contacts.CONTENT_URI
        val FILTER_URI: Uri = Contacts.CONTENT_FILTER_URI
        val REQUIRED_PERMISSIONS = arrayOf(READ_CONTACTS)
        val PROJECTION: Array<String> = arrayOf(
            Contacts._ID,
            Contacts.STARRED,
            Contacts.LOOKUP_KEY,
            Contacts.DISPLAY_NAME_PRIMARY,
            Contacts.PHOTO_THUMBNAIL_URI,
        )
        const val SORT_ORDER = "${Contacts.SORT_KEY_PRIMARY} ASC"
    }

    override val uri: Uri = URI
    override val filterUri: Uri = FILTER_URI
    override val sortOrder: String = SORT_ORDER
    override val selectionArgs: Array<String>? = null
    override val projection: Array<String> = PROJECTION
    override val selection = SelectionBuilder()
        .addNotNull(Contacts.DISPLAY_NAME_PRIMARY)
        .addSelection(Contacts._ID, contactId)
        .build()

    override fun convertCursorToItem(cursor: Cursor): Contact {
        return Contact(
            id = cursor.getLong(cursor.getColumnIndex(Contacts._ID)),
            lookupKey = cursor.getString(cursor.getColumnIndex(Contacts.LOOKUP_KEY)),
            starred = "1" == cursor.getString(cursor.getColumnIndex(Contacts.STARRED)),
            name = cursor.getString(cursor.getColumnIndex(Contacts.DISPLAY_NAME_PRIMARY)),
            photoUri = cursor.getString(cursor.getColumnIndex(Contacts.PHOTO_THUMBNAIL_URI))
        )
    }
}