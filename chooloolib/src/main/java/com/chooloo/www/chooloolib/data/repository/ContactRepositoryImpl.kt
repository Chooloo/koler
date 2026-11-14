package com.chooloo.www.chooloolib.data.repository

import android.content.ContentResolver
import android.content.ContentUris
import android.content.ContentValues
import android.net.Uri
import android.provider.ContactsContract.CommonDataKinds.Email
import android.provider.ContactsContract.CommonDataKinds.Organization
import android.provider.ContactsContract.CommonDataKinds.Phone
import android.provider.ContactsContract.Contacts
import android.provider.ContactsContract.Data
import android.provider.ContactsContract.RawContacts
import androidx.core.net.toUri
import com.chooloo.www.chooloolib.data.source.ContentException
import com.chooloo.www.chooloolib.data.source.ContentQuery
import com.chooloo.www.chooloolib.data.source.ContentSource
import com.chooloo.www.chooloolib.data.source.CursorRow
import com.chooloo.www.chooloolib.di.IoDispatcher
import com.chooloo.www.chooloolib.domain.model.Contact
import com.chooloo.www.chooloolib.domain.model.ContactDetails
import com.chooloo.www.chooloolib.domain.model.Phone as PhoneModel
import com.chooloo.www.chooloolib.domain.repository.ContactRepository
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

@Singleton
class ContactRepositoryImpl @Inject constructor(
    private val contentResolver: ContentResolver,
    private val contentSource: ContentSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : ContactRepository {
    override fun contacts(filter: String): Flow<List<Contact>> = contentSource.observeAll(
        ContentQuery(
            uri = if (filter.isEmpty()) {
                Contacts.CONTENT_URI
            } else {
                Uri.withAppendedPath(Contacts.CONTENT_FILTER_URI, Uri.encode(filter))
            },
            projection = PROJECTION,
            selection = "${Contacts.DISPLAY_NAME_PRIMARY} IS NOT NULL",
            sortOrder = "${Contacts.SORT_KEY_PRIMARY} ASC",
        ),
        ::toContact,
    )
        .map { contacts ->
            val numbers = primaryNumbers()
            contacts.map { it.copy(number = numbers[it.id]) }
        }
        .flowOn(ioDispatcher)

    override fun contact(contactId: Long): Flow<Contact?> = contentSource.observeAll(
        ContentQuery(
            uri = ContentUris.withAppendedId(Contacts.CONTENT_URI, contactId),
            projection = PROJECTION,
        ),
        ::toContact,
    )
        .map { contacts ->
            contacts.firstOrNull()?.let { it.copy(number = primaryNumbers(contactId)[it.id]) }
        }
        .flowOn(ioDispatcher)

    override fun details(contactId: Long): Flow<ContactDetails> = combine(
        contentSource.observeAll(
            ContentQuery(
                uri = Phone.CONTENT_URI,
                projection = arrayOf(Phone.NUMBER, Phone.TYPE, Phone.LABEL),
                selection = "${Phone.CONTACT_ID} = ?",
                selectionArgs = arrayOf(contactId.toString()),
                sortOrder = PRIMARY_FIRST,
            ),
        ) { row ->
            PhoneModel(
                number = row.string(Phone.NUMBER).orEmpty(),
                type = row.int(Phone.TYPE),
                label = row.string(Phone.LABEL),
            )
        },
        contentSource.observeAll(
            ContentQuery(
                uri = Data.CONTENT_URI,
                projection = arrayOf(Data.MIMETYPE, Data.DATA1),
                selection = "${Data.CONTACT_ID} = ? AND ${Data.MIMETYPE} IN (?, ?, ?)",
                selectionArgs = arrayOf(
                    contactId.toString(),
                    Email.CONTENT_ITEM_TYPE,
                    Organization.CONTENT_ITEM_TYPE,
                    WHATSAPP_MIME_TYPE,
                ),
            ),
        ) { row ->
            row.string(Data.MIMETYPE) to row.string(Data.DATA1)?.takeIf(String::isNotBlank)
        },
    ) { phones, rows ->
        fun dataOf(mimeType: String) = rows.filter { it.first == mimeType }.mapNotNull { it.second }
        ContactDetails(
            phones = phones.distinctBy { it.number },
            emails = dataOf(Email.CONTENT_ITEM_TYPE).distinct(),
            organization = dataOf(Organization.CONTENT_ITEM_TYPE).firstOrNull(),
            whatsappNumber = dataOf(WHATSAPP_MIME_TYPE).firstOrNull()?.let(::whatsappNumberOf),
        )
    }.flowOn(ioDispatcher)

    override suspend fun contactIdOf(link: String): Long? = withContext(ioDispatcher) {
        val uri = link.toUri()
        val lookupUri = if (contentResolver.getType(uri) == RawContacts.CONTENT_ITEM_TYPE) {
            RawContacts.getContactLookupUri(contentResolver, uri)
        } else {
            uri
        }
        lookupUri?.let { Contacts.lookupContact(contentResolver, it) }?.let(ContentUris::parseId)
    }

    override suspend fun deleteContact(contactId: Long) {
        val deleted = withContext(ioDispatcher) {
            contentResolver.delete(
                ContentUris.withAppendedId(Contacts.CONTENT_URI, contactId),
                null,
                null,
            )
        }
        if (deleted == 0) throw ContentException.RowNotChanged(Contacts.CONTENT_URI, contactId)
    }

    override suspend fun setFavorite(contactId: Long, isFavorite: Boolean) {
        val values = ContentValues().apply { put(Contacts.STARRED, if (isFavorite) 1 else 0) }
        val updated = withContext(ioDispatcher) {
            contentResolver.update(
                ContentUris.withAppendedId(Contacts.CONTENT_URI, contactId),
                values,
                null,
                null,
            )
        }
        if (updated == 0) throw ContentException.RowNotChanged(Contacts.CONTENT_URI, contactId)
    }

    private fun primaryNumbers(contactId: Long? = null): Map<Long, String> = contentSource.queryAll(
        ContentQuery(
            uri = Phone.CONTENT_URI,
            projection = arrayOf(Phone.CONTACT_ID, Phone.NUMBER),
            selection = contactId?.let { "${Phone.CONTACT_ID} = ?" },
            selectionArgs = contactId?.let { arrayOf(it.toString()) },
            sortOrder = PRIMARY_FIRST,
        ),
    ) { it.long(Phone.CONTACT_ID) to it.string(Phone.NUMBER) }
        .filter { it.second != null }
        .distinctBy { it.first }
        .associate { it.first to it.second!! }

    private fun toContact(row: CursorRow) = Contact(
        id = row.long(Contacts._ID),
        name = row.string(Contacts.DISPLAY_NAME_PRIMARY),
        photoUri = row.string(Contacts.PHOTO_URI) ?: row.string(Contacts.PHOTO_THUMBNAIL_URI),
        isFavorite = row.int(Contacts.STARRED) == 1,
    )

    companion object {
        private const val WHATSAPP_MIME_TYPE = "vnd.android.cursor.item/vnd.com.whatsapp.profile"
        private const val PRIMARY_FIRST = "${Phone.IS_SUPER_PRIMARY} DESC, ${Phone.IS_PRIMARY} DESC"
        private val PROJECTION = arrayOf(
            Contacts._ID,
            Contacts.STARRED,
            Contacts.PHOTO_URI,
            Contacts.PHOTO_THUMBNAIL_URI,
            Contacts.DISPLAY_NAME_PRIMARY,
        )

        fun whatsappNumberOf(data: String): String? =
            data.substringBefore('@').filter(Char::isDigit).takeIf(String::isNotEmpty)
    }
}
