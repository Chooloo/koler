package com.chooloo.www.chooloolib.data.repository

import android.Manifest.permission.READ_CONTACTS
import android.content.ContentResolver
import android.content.Context
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.database.Cursor
import android.net.Uri
import android.provider.ContactsContract.CommonDataKinds.Phone
import android.provider.ContactsContract.PhoneLookup as PhoneLookupContract
import android.util.Log
import com.chooloo.www.chooloolib.data.source.ContentQuery
import com.chooloo.www.chooloolib.data.source.int
import com.chooloo.www.chooloolib.data.source.long
import com.chooloo.www.chooloolib.data.source.observeAll
import com.chooloo.www.chooloolib.data.source.queryAll
import com.chooloo.www.chooloolib.data.source.string
import com.chooloo.www.chooloolib.data.source.withFilter
import com.chooloo.www.chooloolib.di.IoDispatcher
import com.chooloo.www.chooloolib.domain.model.Phone as PhoneModel
import com.chooloo.www.chooloolib.domain.model.PhoneLookup
import com.chooloo.www.chooloolib.domain.repository.PhoneRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

@Singleton
class PhoneRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val contentResolver: ContentResolver,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : PhoneRepository {
    override fun phones(contactId: Long?, filter: String?): Flow<List<PhoneModel>> =
        contentResolver.observeAll(
            ContentQuery(
                uri = Phone.CONTENT_URI.withFilter(Phone.CONTENT_FILTER_URI, filter),
                projection = arrayOf(
                    Phone._ID,
                    Phone.TYPE,
                    Phone.LABEL,
                    Phone.NUMBER,
                    Phone.STARRED,
                    Phone.CONTACT_ID,
                    Phone.PHOTO_THUMBNAIL_URI,
                ),
                selection = contactId?.let { "${Phone.CONTACT_ID} = ?" },
                selectionArgs = contactId?.let { arrayOf(it.toString()) },
                sortOrder = "${Phone.IS_SUPER_PRIMARY} DESC, ${Phone.IS_PRIMARY} DESC",
            ),
            ::toPhone
        ).flowOn(ioDispatcher)

    override suspend fun lookup(number: String): PhoneLookup? = withContext(ioDispatcher) {
        if (number.isBlank() || context.checkSelfPermission(READ_CONTACTS) != PERMISSION_GRANTED) {
            return@withContext null
        }
        runCatching {
            contentResolver.queryAll(
                ContentQuery(
                    uri = Uri.withAppendedPath(PhoneLookupContract.CONTENT_FILTER_URI, Uri.encode(number)),
                    projection = arrayOf(
                        PhoneLookupContract.TYPE,
                        PhoneLookupContract.LABEL,
                        PhoneLookupContract.NUMBER,
                        PhoneLookupContract.PHOTO_URI,
                        PhoneLookupContract.CONTACT_ID,
                        PhoneLookupContract.DISPLAY_NAME,
                    ),
                ),
                ::toPhoneLookup
            ).firstOrNull()
        }.onFailure { Log.e(TAG, "Contact lookup failed", it) }.getOrNull()
    }

    private fun toPhone(cursor: Cursor) = PhoneModel(
        id = cursor.long(Phone._ID),
        contactId = cursor.long(Phone.CONTACT_ID),
        number = cursor.string(Phone.NUMBER).orEmpty(),
        label = cursor.string(Phone.LABEL),
        type = cursor.int(Phone.TYPE),
        photoUri = cursor.string(Phone.PHOTO_THUMBNAIL_URI),
        isFavorite = cursor.int(Phone.STARRED) == 1,
    )

    private fun toPhoneLookup(cursor: Cursor) = PhoneLookup(
        contactId = cursor.long(PhoneLookupContract.CONTACT_ID),
        name = cursor.string(PhoneLookupContract.DISPLAY_NAME),
        number = cursor.string(PhoneLookupContract.NUMBER),
        label = cursor.string(PhoneLookupContract.LABEL),
        type = cursor.int(PhoneLookupContract.TYPE),
        photoUri = cursor.string(PhoneLookupContract.PHOTO_URI),
    )

    private companion object {
        const val TAG = "PhoneRepository"
    }
}
