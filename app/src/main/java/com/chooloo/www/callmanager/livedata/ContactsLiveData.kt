package com.chooloo.www.callmanager.livedata

import android.content.Context
import android.net.Uri
import android.provider.ContactsContract

class ContactsLiveData(context: Context) : CursorLiveData(context) {

    companion object {
        private const val COLUMN_ID = ContactsContract.Contacts._ID
        private const val COLUMN_NAME = ContactsContract.Contacts.DISPLAY_NAME_PRIMARY
        private const val COLUMN_THUMBNAIL = ContactsContract.Contacts.PHOTO_THUMBNAIL_URI
        private const val COLUMN_STARRED = ContactsContract.Contacts.STARRED

        private const val CONTACTS_ORDER = ContactsContract.Contacts.SORT_KEY_PRIMARY + " ASC"
        private const val CONTACTS_SELECTION = ContactsContract.Contacts.DISPLAY_NAME_PRIMARY + " IS NOT NULL"
        private val CONTACTS_SELECTION_ARGS = null
        private val CONTACTS_URI = ContactsContract.Contacts.CONTENT_URI
        private val CONTACTS_PROJECTION = arrayOf(
                COLUMN_ID,
                COLUMN_NAME,
                COLUMN_THUMBNAIL,
                COLUMN_STARRED
        )
    }

//    override fun onGetUri(): Uri = (
//            when (_searchString) {
//                null -> ContactsContract.Contacts.CONTENT_URI.buildUpon()
//                else -> Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_FILTER_URI, Uri.encode(PhoneNumberUtils.normalizeNumber(_searchString))).buildUpon()
//            }.apply {
//                appendQueryParameter(ContactsContract.REMOVE_DUPLICATE_ENTRIES, "true")
//                appendQueryParameter(ContactsContract.Contacts.EXTRA_ADDRESS_BOOK_INDEX, "true")
//            }).build()

//    fun setSearchText(text: String) {
//        _searchString = text
//    }

    override fun onGetUri(): Uri = CONTACTS_URI.buildUpon().apply {
        appendQueryParameter(ContactsContract.REMOVE_DUPLICATE_ENTRIES, "true")
        appendQueryParameter(ContactsContract.Contacts.EXTRA_ADDRESS_BOOK_INDEX, "true")
    }.build()

    override fun onGetProjection(): Array<String>? = CONTACTS_PROJECTION
    override fun onGetSelection(): String? = CONTACTS_SELECTION
    override fun onGetSelectionArgs(): Array<String>? = CONTACTS_SELECTION_ARGS
    override fun onGetSortOrder(): String? = CONTACTS_ORDER
}