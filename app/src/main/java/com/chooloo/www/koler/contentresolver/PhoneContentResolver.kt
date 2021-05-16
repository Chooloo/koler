package com.chooloo.www.koler.contentresolver

import PhoneAccount
import android.Manifest.permission.READ_CONTACTS
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.ContactsContract.CommonDataKinds.Phone
import com.chooloo.www.koler.data.PhonesBundle

class PhoneContentResolver(
    context: Context,
    contactId: Long? = null
) : BaseContentResolver<PhonesBundle>(context) {
    override val requiredPermissions = arrayOf(READ_CONTACTS)

    override val uri: Uri = Phone.CONTENT_URI

    override val filterUri: Uri = Phone.CONTENT_FILTER_URI

    override val selection = SelectionBuilder().addSelection(Phone.CONTACT_ID, contactId).build()

    override val sortOrder: String? = null

    override val projection = arrayOf(
        Phone.TYPE,
        Phone.NUMBER,
        Phone.CONTACT_ID,
        Phone.NORMALIZED_NUMBER,
        Phone.DISPLAY_NAME_PRIMARY
    )

    override val selectionArgs: Array<String>? = null

    override fun convertCursorToContent(cursor: Cursor?) =
        PhonesBundle(
            phoneAccounts = ArrayList<PhoneAccount>().apply {
                while (cursor != null && cursor.moveToNext()) {
                    cursor.apply {
                        add(
                            PhoneAccount(
                                number = getString(getColumnIndex(Phone.NUMBER)),
                                contactId = getLong(getColumnIndex(Phone.CONTACT_ID)),
                                displayName = getString(getColumnIndex(Phone.DISPLAY_NAME_PRIMARY)),
                                type = getInt(getColumnIndex(Phone.TYPE)),
                                normalizedNumber = getString(getColumnIndex(Phone.NORMALIZED_NUMBER))
                                    ?: getString(getColumnIndex(Phone.NUMBER))
                            )
                        )
                    }
                }
            }
        )
}