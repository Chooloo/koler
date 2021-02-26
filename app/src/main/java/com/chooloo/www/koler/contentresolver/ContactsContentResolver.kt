package com.chooloo.www.koler.contentresolver

import android.Manifest.permission.READ_CONTACTS
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.ContactsContract.CommonDataKinds.Callable.EXTRA_ADDRESS_BOOK_INDEX_COUNTS
import android.provider.ContactsContract.CommonDataKinds.Callable.EXTRA_ADDRESS_BOOK_INDEX_TITLES
import android.provider.ContactsContract.Contacts
import android.provider.ContactsContract.REMOVE_DUPLICATE_ENTRIES
import com.chooloo.www.koler.entity.Contact
import com.chooloo.www.koler.entity.ContactsBundle

class ContactsContentResolver(
    context: Context
) : BaseContentResolver<ContactsBundle>(context) {

    override val requiredPermissions = arrayOf(READ_CONTACTS)

    override fun onGetUri(): Uri = Contacts.CONTENT_URI
        .buildUpon()
        .appendQueryParameter(Contacts.EXTRA_ADDRESS_BOOK_INDEX, "true")
        .appendQueryParameter(REMOVE_DUPLICATE_ENTRIES, "true")
        .build()

    override fun onGetFilterUri(): Uri = Contacts.CONTENT_FILTER_URI
        .buildUpon()
        .appendQueryParameter(Contacts.EXTRA_ADDRESS_BOOK_INDEX, "true")
        .appendQueryParameter(REMOVE_DUPLICATE_ENTRIES, "true")
        .build()

    override fun onGetSelection() = "${Contacts.DISPLAY_NAME_PRIMARY} IS NOT NULL"
    override fun onGetSortOrder() = "${Contacts.SORT_KEY_PRIMARY} ASC"
    override fun onGetProjection() = arrayOf(
        Contacts._ID,
        Contacts.DISPLAY_NAME_PRIMARY,
        Contacts.PHOTO_THUMBNAIL_URI,
        Contacts.STARRED,
        Contacts.LOOKUP_KEY
    )

    override fun convertCursorToContent(cursor: Cursor?) = ContactsBundle(
        contacts = ArrayList<Contact>().apply {
            while (cursor != null && cursor.moveToNext()) cursor.apply {
                add(
                    Contact(
                        id = getLong(getColumnIndex(Contacts._ID)),
                        name = getString(getColumnIndex(Contacts.DISPLAY_NAME_PRIMARY)),
                        photoUri = getString(getColumnIndex(Contacts.PHOTO_THUMBNAIL_URI)),
                        starred = "1" == getString(getColumnIndex(Contacts.STARRED)),
                        lookupKey = getString(getColumnIndex(Contacts.LOOKUP_KEY))
                    )
                )
            }
        }.toTypedArray(),
        headers = cursor?.extras?.getStringArray(EXTRA_ADDRESS_BOOK_INDEX_TITLES)
            ?: arrayOf(),
        headersCounts = cursor?.extras?.getIntArray(EXTRA_ADDRESS_BOOK_INDEX_COUNTS)?.toTypedArray()
            ?: arrayOf(),
    ).also {
        cursor?.close()
    }
}