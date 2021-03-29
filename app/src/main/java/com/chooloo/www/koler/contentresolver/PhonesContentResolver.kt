package com.chooloo.www.koler.contentresolver

import PhoneAccount
import android.Manifest.permission.READ_CONTACTS
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.ContactsContract.CommonDataKinds.Phone
import com.chooloo.www.koler.data.PhonesBundle

class PhonesContentResolver(
    context: Context,
    private val contactId: Long? = null
) : BaseContentResolver<PhonesBundle>(context) {
    override val requiredPermissions = arrayOf(READ_CONTACTS)

    override fun onGetUri(): Uri = Phone.CONTENT_URI

    override fun onGetFilterUri(): Uri = Phone.CONTENT_FILTER_URI

    override fun onGetProjection() = arrayOf(
        Phone.CONTACT_ID,
        Phone.DISPLAY_NAME_PRIMARY,
        Phone.NUMBER,
        Phone.STARRED,
        Phone.PHOTO_URI,
        Phone.TYPE
    )

    override fun onGetSelection() = SelectionBuilder()
        .addSelection(Phone.CONTACT_ID, contactId)
        .build()

    override fun convertCursorToContent(cursor: Cursor?) =
        PhonesBundle(
            phoneAccounts = ArrayList<PhoneAccount>().apply {
                while (cursor != null && cursor.moveToNext()) cursor.apply {
                    add(
                        PhoneAccount(
                            number = getString(getColumnIndex(Phone.NUMBER)),
                            contactId = getLong(getColumnIndex(Phone.CONTACT_ID)),
                            photoUri = getString(getColumnIndex(Phone.PHOTO_URI)),
                            starred = "1" == getString(getColumnIndex(Phone.STARRED)),
                            displayName = getString(getColumnIndex(Phone.DISPLAY_NAME_PRIMARY)),
                            type = PhoneAccount.PhoneAccountType.fromType(
                                getInt(getColumnIndex(Phone.TYPE))
                            )
                        )
                    )
                }
            }.toTypedArray()
        )
}