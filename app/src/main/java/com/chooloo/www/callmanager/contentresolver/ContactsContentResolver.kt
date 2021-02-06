package com.chooloo.www.callmanager.contentresolver

import android.Manifest.permission.READ_CONTACTS
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.ContactsContract
import androidx.core.content.ContentResolverCompat
import androidx.core.os.CancellationSignal
import com.chooloo.www.callmanager.entity.Contact

class ContactsContentResolver(context: Context) : BaseContentResolver<Array<Contact>>(context, URI) {

    companion object {
        val REQUIRED_PERMISSION = READ_CONTACTS

        private const val COLUMN_ID = ContactsContract.Contacts._ID
        private const val COLUMN_NAME = ContactsContract.Contacts.DISPLAY_NAME_PRIMARY
        private const val COLUMN_THUMBNAIL = ContactsContract.Contacts.PHOTO_THUMBNAIL_URI
        private const val COLUMN_STARRED = ContactsContract.Contacts.STARRED

        private val URI = ContactsContract.Contacts.CONTENT_URI
        private const val SORT_ORDER = ContactsContract.Contacts.SORT_KEY_PRIMARY + " ASC"
        private const val SELECTION = ContactsContract.Contacts.DISPLAY_NAME_PRIMARY + " IS NOT NULL"
        private val PROJECTION = arrayOf(
                COLUMN_ID,
                COLUMN_NAME,
                COLUMN_THUMBNAIL,
                COLUMN_STARRED
        )

        fun getUri(): Uri {
            return ContactsContract.Contacts.CONTENT_URI.buildUpon().apply {
                appendQueryParameter(ContactsContract.REMOVE_DUPLICATE_ENTRIES, "true")
                appendQueryParameter(ContactsContract.Contacts.EXTRA_ADDRESS_BOOK_INDEX, "true")
            }.build()
        }

        fun getContactsCursor(context: Context): Cursor {
            return ContentResolverCompat.query(
                    context.contentResolver,
                    getUri(),
                    PROJECTION,
                    SELECTION,
                    null,
                    SORT_ORDER,
                    CancellationSignal()
            )
        }

        fun getContacts(context: Context): Array<Contact> {
            val contacts: MutableList<Contact> = ArrayList()
            val cursor = getContactsCursor(context)
            while (cursor.moveToNext()) {
                contacts.add(Contact(
                        contactId = cursor.getLong(cursor.getColumnIndex(COLUMN_ID)),
                        name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME)),
                        photoUri = cursor.getString(cursor.getColumnIndex(COLUMN_THUMBNAIL)),
                        starred = "1" == cursor.getString(cursor.getColumnIndex(COLUMN_STARRED))))
            }
            return contacts.toTypedArray()
        }
    }

    override fun getContent(): Array<Contact> {
        return getContacts(context)
    }
}