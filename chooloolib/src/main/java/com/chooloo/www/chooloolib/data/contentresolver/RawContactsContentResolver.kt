package com.chooloo.www.chooloolib.data.contentresolver

import android.content.ContentResolver
import android.database.Cursor
import android.net.Uri
import android.provider.ContactsContract.Contacts
import com.chooloo.www.chooloolib.domain.model.record.RawContactRecord
import com.chooloo.www.chooloolib.utils.getLong
import com.chooloo.www.chooloolib.utils.getString

class RawContactsContentResolver(
    contactId: Long,
    filter: String? = null,
    contentResolver: ContentResolver
) : BaseContentResolver<RawContactRecord>(filter, contentResolver) {

    override val uri: Uri = Uri.withAppendedPath(
        Uri.withAppendedPath(Contacts.CONTENT_URI, contactId.toString()),
        Contacts.Entity.CONTENT_DIRECTORY
    )
    override val filterUri: Uri? = null
    override val selection: String? = null
    override val selectionArgs: Array<String>? = null
    override val sortOrder: String = Contacts.Entity.RAW_CONTACT_ID
    override val projection: Array<String> = arrayOf(
        Contacts.Entity.DATA1,
        Contacts.Entity.DATA3,
        Contacts.Entity.MIMETYPE,
        Contacts.Entity.CONTACT_ID,
        Contacts.Entity.ACCOUNT_NAME,
        Contacts.Entity.ACCOUNT_TYPE,
        Contacts.Entity.RAW_CONTACT_ID
    )

    override fun convertCursorToItem(cursor: Cursor) = RawContactRecord(
        data = cursor.getString(Contacts.Entity.DATA1),
        label = cursor.getString(Contacts.Entity.DATA3),
        id = cursor.getLong(Contacts.Entity.RAW_CONTACT_ID),
        contactId = cursor.getLong(Contacts.Entity.CONTACT_ID),
        accountName = cursor.getString(Contacts.Entity.ACCOUNT_NAME),
        accountType = cursor.getString(Contacts.Entity.ACCOUNT_TYPE),
        type = RawContactRecord.RawContactType.fromContentType(
            cursor.getString(Contacts.Entity.MIMETYPE).orEmpty()
        )
    )
}
