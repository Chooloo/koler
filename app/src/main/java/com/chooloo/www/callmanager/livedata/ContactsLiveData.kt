package com.chooloo.www.callmanager.livedata

import android.content.Context
import android.database.ContentObserver
import android.database.Cursor
import android.provider.ContactsContract
import androidx.core.content.ContentResolverCompat
import androidx.core.os.CancellationSignal
import androidx.lifecycle.LiveData
import com.chooloo.www.callmanager.cursorloader.ContactsCursorLoader
import com.chooloo.www.callmanager.entity.Contact
import timber.log.Timber

class ContactsLiveData(
        private val context: Context
) : LiveData<Array<Contact>>() {

    companion object {
        private const val COLUMN_ID = ContactsContract.Contacts._ID
        private const val COLUMN_NAME = ContactsContract.Contacts.DISPLAY_NAME_PRIMARY
        private const val COLUMN_THUMBNAIL = ContactsContract.Contacts.PHOTO_THUMBNAIL_URI
        private const val COLUMN_STARRED = ContactsContract.Contacts.STARRED

        private const val CONTACTS_SORT_ORDER = ContactsContract.Contacts.SORT_KEY_PRIMARY + " ASC"
        private const val CONTACTS_SELECTION = ContactsContract.Contacts.DISPLAY_NAME_PRIMARY + " IS NOT NULL"
        private val CONTACTS_SELECTION_ARGS = null
        private val CONTACTS_URI = ContactsContract.Contacts.CONTENT_URI.buildUpon().apply {
            appendQueryParameter(ContactsContract.REMOVE_DUPLICATE_ENTRIES, "true")
            appendQueryParameter(ContactsContract.Contacts.EXTRA_ADDRESS_BOOK_INDEX, "true")
        }.build()

        private val CONTACTS_PROJECTION = arrayOf(
                COLUMN_ID,
                COLUMN_NAME,
                COLUMN_THUMBNAIL,
                COLUMN_STARRED
        )

    }

    private lateinit var _observer: CursorContentObserver

    init {
        Timber.d("CONTACTS URI " + CONTACTS_URI)
    }

    override fun onActive() {
        _observer = CursorContentObserver()
        context.contentResolver.registerContentObserver(CONTACTS_URI, true, _observer)
    }

    override fun onInactive() {
        context.contentResolver.unregisterContentObserver(_observer)
    }

    private fun onContactsCursorChanged() {
        postValue(getContacts())
    }

    private fun getContacts(): Array<Contact> {
        val contacts: MutableList<Contact> = ArrayList()
        val cursor = getContactsCursor()
        while (cursor.moveToNext()) {
            contacts.add(Contact(
                    contactId = cursor.getLong(cursor.getColumnIndex(COLUMN_ID)),
                    name = cursor.getString(cursor.getColumnIndex(ContactsCursorLoader.COLUMN_NAME)),
                    photoUri = cursor.getString(cursor.getColumnIndex(ContactsCursorLoader.COLUMN_THUMBNAIL)),
                    starred = "1" == cursor.getString(cursor.getColumnIndex(ContactsCursorLoader.COLUMN_STARRED))))
        }
        return contacts.toTypedArray()
    }

    private fun getContactsCursor(): Cursor {
        return ContentResolverCompat.query(
                context.contentResolver,
                CONTACTS_URI,
                CONTACTS_PROJECTION,
                CONTACTS_SELECTION,
                CONTACTS_SELECTION_ARGS,
                CONTACTS_SORT_ORDER,
                CancellationSignal()
        ).apply {

        }
    }

    inner class CursorContentObserver : ContentObserver(null) {
        override fun onChange(selfChange: Boolean) {
            onContactsCursorChanged()
        }
    }
}