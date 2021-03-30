package com.chooloo.www.koler.contentresolver

import PhoneAccount.PhoneAccountType
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.ContactsContract.PhoneLookup
import com.chooloo.www.koler.data.PhoneLookupAccount

class PhoneLookupContentResolver(
    context: Context,
    private val number: String?,
) : BaseContentResolver<Array<PhoneLookupAccount>>(context) {
    override val uri: Uri = PhoneLookup.CONTENT_FILTER_URI
        .buildUpon()
        .appendPath(number)
        .build()

    override val filterUri: Uri = PhoneLookup.CONTENT_FILTER_URI

    override val selection: String? = null

    override val sortOrder: String? = null

    override val projection = arrayOf(
        PhoneLookup.TYPE,
        PhoneLookup.NUMBER,
        PhoneLookup.STARRED,
        PhoneLookup.PHOTO_URI,
        PhoneLookup.CONTACT_ID,
        PhoneLookup.DISPLAY_NAME,
    )

    override val selectionArgs: Array<String>? = null

    override fun convertCursorToContent(cursor: Cursor?) =
        ArrayList<PhoneLookupAccount>().apply {
            while (cursor != null && cursor.moveToNext()) cursor.apply {
                add(
                    PhoneLookupAccount(
                        number = getString(getColumnIndex(PhoneLookup.NUMBER)),
                        name = getString(getColumnIndex(PhoneLookup.DISPLAY_NAME)),
                        contactId = getLong(getColumnIndex(PhoneLookup.CONTACT_ID)),
                        photoUri = getString(getColumnIndex(PhoneLookup.PHOTO_URI)),
                        starred = "1" == getString(getColumnIndex(PhoneLookup.STARRED)),
                        type = PhoneAccountType.fromType(getInt(getColumnIndex(PhoneLookup.TYPE)))
                    )
                )
            }
        }.toTypedArray()
}