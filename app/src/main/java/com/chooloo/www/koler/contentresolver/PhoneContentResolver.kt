package com.chooloo.www.koler.contentresolver

import PhoneAccount
import android.Manifest.permission.READ_CONTACTS
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.ContactsContract.CommonDataKinds.Phone

class PhoneContentResolver(
    context: Context,
    contactId: Long? = null
) : BaseItemsContentResolver<PhoneAccount>(context) {
    companion object {
        val URI: Uri = Phone.CONTENT_URI
        val FILTER_URI: Uri = Phone.CONTENT_FILTER_URI
        val REQUIRED_PERMISSIONS = arrayOf(READ_CONTACTS)
        val PROJECTION: Array<String> = arrayOf(
            Phone.TYPE,
            Phone.NUMBER,
            Phone.CONTACT_ID,
            Phone.NORMALIZED_NUMBER,
            Phone.DISPLAY_NAME_PRIMARY
        )
    }

    override val uri: Uri = URI
    override val sortOrder: String? = null
    override val filterUri: Uri = FILTER_URI
    override val selectionArgs: Array<String>? = null
    override val projection: Array<String> = PROJECTION
    override val selection = SelectionBuilder().addSelection(Phone.CONTACT_ID, contactId).build()

    override fun convertCursorToItem(cursor: Cursor): PhoneAccount {
        return PhoneAccount(
            type = cursor.getInt(cursor.getColumnIndex(Phone.TYPE)),
            number = cursor.getString(cursor.getColumnIndex(Phone.NUMBER)),
            contactId = cursor.getLong(cursor.getColumnIndex(Phone.CONTACT_ID)),
            displayName = cursor.getString(cursor.getColumnIndex(Phone.DISPLAY_NAME_PRIMARY)),
            normalizedNumber = cursor.getString(cursor.getColumnIndex(Phone.NORMALIZED_NUMBER))
                ?: cursor.getString(cursor.getColumnIndex(Phone.NUMBER))
        )
    }
}