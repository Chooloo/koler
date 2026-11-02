package com.chooloo.www.chooloolib.data.repository

import android.content.ContentResolver
import android.content.ContentUris
import android.database.Cursor
import android.net.Uri
import android.provider.ContactsContract.CommonDataKinds.Email
import android.provider.ContactsContract.CommonDataKinds.Im
import android.provider.ContactsContract.CommonDataKinds.Organization
import android.provider.ContactsContract.CommonDataKinds.Phone
import android.provider.ContactsContract.CommonDataKinds.StructuredName
import android.provider.ContactsContract.Contacts
import com.chooloo.www.chooloolib.data.source.ContentQuery
import com.chooloo.www.chooloolib.data.source.long
import com.chooloo.www.chooloolib.data.source.observeAll
import com.chooloo.www.chooloolib.data.source.string
import com.chooloo.www.chooloolib.di.IoDispatcher
import com.chooloo.www.chooloolib.domain.model.ContactAccount
import com.chooloo.www.chooloolib.domain.repository.ContactAccountRepository
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

@Singleton
class ContactAccountRepositoryImpl @Inject constructor(
    private val contentResolver: ContentResolver,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : ContactAccountRepository {
    override fun accounts(contactId: Long): Flow<List<ContactAccount>> =
        contentResolver.observeAll(
            ContentQuery(
                uri = Uri.withAppendedPath(
                    ContentUris.withAppendedId(Contacts.CONTENT_URI, contactId),
                    Contacts.Entity.CONTENT_DIRECTORY
                ),
                projection = arrayOf(
                    Contacts.Entity.DATA1,
                    Contacts.Entity.DATA3,
                    Contacts.Entity.MIMETYPE,
                    Contacts.Entity.CONTACT_ID,
                    Contacts.Entity.ACCOUNT_NAME,
                    Contacts.Entity.ACCOUNT_TYPE,
                    Contacts.Entity.RAW_CONTACT_ID,
                ),
                sortOrder = Contacts.Entity.RAW_CONTACT_ID,
            ),
            ::toAccount
        ).flowOn(ioDispatcher)

    private fun toAccount(cursor: Cursor) = ContactAccount(
        id = cursor.long(Contacts.Entity.RAW_CONTACT_ID),
        contactId = cursor.long(Contacts.Entity.CONTACT_ID),
        type = typeOf(cursor.string(Contacts.Entity.MIMETYPE)),
        data = cursor.string(Contacts.Entity.DATA1),
        label = cursor.string(Contacts.Entity.DATA3),
        accountName = cursor.string(Contacts.Entity.ACCOUNT_NAME),
        accountType = cursor.string(Contacts.Entity.ACCOUNT_TYPE),
    )

    private fun typeOf(mimeType: String?) = when (mimeType) {
        Im.CONTENT_ITEM_TYPE -> ContactAccount.Type.IM
        Phone.CONTENT_ITEM_TYPE -> ContactAccount.Type.PHONE
        Email.CONTENT_ITEM_TYPE -> ContactAccount.Type.EMAIL
        StructuredName.CONTENT_ITEM_TYPE -> ContactAccount.Type.NAME
        Organization.CONTENT_ITEM_TYPE -> ContactAccount.Type.ORGANIZATION
        WHATSAPP_MIME_TYPE -> ContactAccount.Type.WHATSAPP
        else -> ContactAccount.Type.CUSTOM
    }

    private companion object {
        const val WHATSAPP_MIME_TYPE = "vnd.android.cursor.item/vnd.com.whatsapp.profile"
    }
}
