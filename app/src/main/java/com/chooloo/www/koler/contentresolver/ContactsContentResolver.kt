package com.chooloo.www.koler.contentresolver

import android.Manifest.permission.READ_CONTACTS
import android.content.Context
import android.database.Cursor
import android.provider.ContactsContract
import android.provider.ContactsContract.Contacts
import com.chooloo.www.koler.entity.Contact

class ContactsContentResolver(context: Context) : BaseContentResolver<Array<Contact>>(context) {

    override val requiredPermissions: Array<String>
        get() = arrayOf(READ_CONTACTS)

    override fun onGetDefaultUri() = Contacts.CONTENT_URI.buildUpon().appendQueryParameter(ContactsContract.REMOVE_DUPLICATE_ENTRIES, "true").build()
    override fun onGetFilterUri() = Contacts.CONTENT_FILTER_URI
    override fun onGetSelection() = "${Contacts.DISPLAY_NAME_PRIMARY} IS NOT NULL"
    override fun onGetSortOrder() = "${Contacts.SORT_KEY_PRIMARY} ASC"
    override fun onGetSelectionArgs() = null
    override fun onGetProjection() = arrayOf(
            Contacts._ID,
            Contacts.DISPLAY_NAME_PRIMARY,
            Contacts.PHOTO_THUMBNAIL_URI,
            Contacts.STARRED
    )

    override fun convertCursorToContent(cursor: Cursor?) = ArrayList<Contact>().apply {
        while (cursor != null && cursor.moveToNext()) cursor.apply {
            add(Contact(
                    contactId = getLong(getColumnIndex(Contacts._ID)),
                    name = getString(getColumnIndex(Contacts.DISPLAY_NAME_PRIMARY)),
                    photoUri = getString(getColumnIndex(Contacts.PHOTO_THUMBNAIL_URI)),
                    starred = "1" == getString(getColumnIndex(Contacts.STARRED))
            ))
        }
        cursor?.close()
    }.toTypedArray()
}