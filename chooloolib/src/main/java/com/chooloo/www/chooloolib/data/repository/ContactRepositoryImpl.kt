package com.chooloo.www.chooloolib.data.repository

import android.content.ContentResolver
import android.content.ContentUris
import android.content.ContentValues
import android.database.Cursor
import android.provider.ContactsContract.CommonDataKinds.Phone
import android.provider.ContactsContract.Contacts
import com.chooloo.www.chooloolib.data.source.ContentQuery
import com.chooloo.www.chooloolib.data.source.int
import com.chooloo.www.chooloolib.data.source.long
import com.chooloo.www.chooloolib.data.source.observeAll
import com.chooloo.www.chooloolib.data.source.queryAll
import com.chooloo.www.chooloolib.data.source.string
import com.chooloo.www.chooloolib.data.source.withFilter
import com.chooloo.www.chooloolib.di.IoDispatcher
import com.chooloo.www.chooloolib.domain.model.Contact
import com.chooloo.www.chooloolib.domain.repository.ContactRepository
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

@Singleton
class ContactRepositoryImpl @Inject constructor(
    private val contentResolver: ContentResolver,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : ContactRepository {
    override fun contacts(filter: String?): Flow<List<Contact>> =
        contentResolver.observeAll(contactsQuery(filter), ::toContact)
            .map { contacts ->
                val numbers = primaryNumbers()
                contacts.map { it.copy(number = numbers[it.id]) }
            }
            .flowOn(ioDispatcher)

    override fun contact(contactId: Long): Flow<Contact?> =
        contentResolver.observeAll(contactQuery(contactId), ::toContact)
            .map { contacts -> contacts.firstOrNull()?.let { it.copy(number = primaryNumbers(contactId)[it.id]) } }
            .flowOn(ioDispatcher)

    override suspend fun deleteContact(contactId: Long) = withContext(ioDispatcher) {
        val deleted = contentResolver.delete(ContentUris.withAppendedId(Contacts.CONTENT_URI, contactId), null, null)
        check(deleted > 0) { "Contact $contactId was not deleted" }
    }

    override suspend fun setFavorite(contactId: Long, isFavorite: Boolean) = withContext(ioDispatcher) {
        val values = ContentValues().apply { put(Contacts.STARRED, if (isFavorite) 1 else 0) }
        val updated = contentResolver.update(ContentUris.withAppendedId(Contacts.CONTENT_URI, contactId), values, null, null)
        check(updated > 0) { "Contact $contactId favorite was not updated" }
    }

    private fun primaryNumbers(contactId: Long? = null): Map<Long, String> =
        contentResolver.queryAll(
            ContentQuery(
                uri = Phone.CONTENT_URI,
                projection = arrayOf(Phone.CONTACT_ID, Phone.NUMBER),
                selection = contactId?.let { "${Phone.CONTACT_ID} = ?" },
                selectionArgs = contactId?.let { arrayOf(it.toString()) },
                sortOrder = "${Phone.IS_SUPER_PRIMARY} DESC, ${Phone.IS_PRIMARY} DESC",
            )
        ) { it.long(Phone.CONTACT_ID) to it.string(Phone.NUMBER) }
            .filter { it.second != null }
            .distinctBy { it.first }
            .associate { it.first to it.second!! }

    private fun contactsQuery(filter: String?) = ContentQuery(
        uri = Contacts.CONTENT_URI.withFilter(Contacts.CONTENT_FILTER_URI, filter),
        projection = PROJECTION,
        selection = "${Contacts.DISPLAY_NAME_PRIMARY} IS NOT NULL",
        sortOrder = "${Contacts.SORT_KEY_PRIMARY} ASC",
    )

    private fun contactQuery(contactId: Long) = ContentQuery(
        uri = ContentUris.withAppendedId(Contacts.CONTENT_URI, contactId),
        projection = PROJECTION,
    )

    private fun toContact(cursor: Cursor) = Contact(
        id = cursor.long(Contacts._ID),
        name = cursor.string(Contacts.DISPLAY_NAME_PRIMARY),
        photoUri = cursor.string(Contacts.PHOTO_URI) ?: cursor.string(Contacts.PHOTO_THUMBNAIL_URI),
        isFavorite = cursor.int(Contacts.STARRED) == 1,
        hasPhoneNumber = cursor.int(Contacts.HAS_PHONE_NUMBER) > 0,
    )

    private companion object {
        val PROJECTION = arrayOf(
            Contacts._ID,
            Contacts.STARRED,
            Contacts.PHOTO_URI,
            Contacts.PHOTO_THUMBNAIL_URI,
            Contacts.HAS_PHONE_NUMBER,
            Contacts.DISPLAY_NAME_PRIMARY,
        )
    }
}
