package com.chooloo.www.koler.contentresolver

import android.Manifest.permission.READ_CONTACTS
import android.content.Context
import android.provider.ContactsContract
import com.chooloo.www.koler.entity.Contact

class ContactsContentResolver(context: Context) : BaseContentResolver<Array<Contact>>(
        context = context,
        defaultUri = ContactsContract.Contacts.CONTENT_URI.buildUpon().appendQueryParameter(ContactsContract.REMOVE_DUPLICATE_ENTRIES, "true").build(),
        filterUri = ContactsContract.Contacts.CONTENT_FILTER_URI,
        selection = "${ContactsContract.Contacts.DISPLAY_NAME_PRIMARY} IS NOT NULL",
        sortOrder = "${ContactsContract.Contacts.SORT_KEY_PRIMARY} ASC",
        projection = arrayOf(COLUMN_ID, COLUMN_NAME, COLUMN_THUMBNAIL, COLUMN_STARRED)
) {

    companion object {
        val REQUIRED_PERMISSIONS = arrayOf(READ_CONTACTS)

        private const val COLUMN_ID = ContactsContract.Contacts._ID
        private const val COLUMN_NAME = ContactsContract.Contacts.DISPLAY_NAME_PRIMARY
        private const val COLUMN_THUMBNAIL = ContactsContract.Contacts.PHOTO_THUMBNAIL_URI
        private const val COLUMN_STARRED = ContactsContract.Contacts.STARRED
    }

    override fun getContent(): Array<Contact> {
        return ArrayList<Contact>().apply {
            val cursor = queryContent()
            while (cursor != null && cursor.moveToNext()) {
                add(Contact(
                        contactId = cursor.getLong(cursor.getColumnIndex(COLUMN_ID)),
                        name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME)),
                        photoUri = cursor.getString(cursor.getColumnIndex(COLUMN_THUMBNAIL)),
                        starred = "1" == cursor.getString(cursor.getColumnIndex(COLUMN_STARRED))
                ))
            }
            cursor?.close()
        }.toTypedArray()
    }
}