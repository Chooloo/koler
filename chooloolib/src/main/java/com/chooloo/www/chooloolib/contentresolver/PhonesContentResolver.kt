package com.chooloo.www.chooloolib.contentresolver

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.ContactsContract.CommonDataKinds.Phone
import com.chooloo.www.chooloolib.model.PhoneAccount
import com.chooloo.www.chooloolib.util.SelectionBuilder

class PhonesContentResolver(context: Context, contactId: Long? = null) :
    BaseContentResolver<PhoneAccount>(context) {

    override val uri: Uri = Phone.CONTENT_URI
    override val sortOrder: String? = null
    override val filterUri: Uri = Phone.CONTENT_FILTER_URI
    override val selectionArgs: Array<String>? = null
    override val selection = SelectionBuilder().addSelection(Phone.CONTACT_ID, contactId).build()
    override val projection: Array<String> = arrayOf(
        Phone.TYPE,
        Phone.LABEL,
        Phone.NUMBER,
        Phone.CONTACT_ID,
        Phone.NORMALIZED_NUMBER,
        Phone.DISPLAY_NAME_PRIMARY
    )

    @SuppressLint("Range")
    override fun convertCursorToItem(cursor: Cursor) = PhoneAccount(
        type = cursor.getInt(cursor.getColumnIndex(Phone.TYPE)),
        label = cursor.getString(cursor.getColumnIndex(Phone.LABEL)),
        number = cursor.getString(cursor.getColumnIndex(Phone.NUMBER)),
        contactId = cursor.getLong(cursor.getColumnIndex(Phone.CONTACT_ID)),
        displayName = cursor.getString(cursor.getColumnIndex(Phone.DISPLAY_NAME_PRIMARY)),
        normalizedNumber = cursor.getString(cursor.getColumnIndex(Phone.NORMALIZED_NUMBER))
    )
}