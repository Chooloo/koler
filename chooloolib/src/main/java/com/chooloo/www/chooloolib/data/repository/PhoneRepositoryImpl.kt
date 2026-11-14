package com.chooloo.www.chooloolib.data.repository

import android.Manifest.permission.READ_CONTACTS
import android.content.Context
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.net.Uri
import android.provider.ContactsContract.PhoneLookup as PhoneLookupContract
import android.util.Log
import com.chooloo.www.chooloolib.data.source.ContentQuery
import com.chooloo.www.chooloolib.data.source.ContentSource
import com.chooloo.www.chooloolib.di.IoDispatcher
import com.chooloo.www.chooloolib.domain.model.PhoneLookup
import com.chooloo.www.chooloolib.domain.repository.PhoneRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

@Singleton
class PhoneRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val contentSource: ContentSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : PhoneRepository {
    override suspend fun lookup(number: String): PhoneLookup? = withContext(ioDispatcher) {
        if (number.isBlank() || context.checkSelfPermission(READ_CONTACTS) != PERMISSION_GRANTED) {
            return@withContext null
        }
        runCatching {
            contentSource.queryAll(
                ContentQuery(
                    uri = Uri.withAppendedPath(
                        PhoneLookupContract.CONTENT_FILTER_URI,
                        Uri.encode(number),
                    ),
                    projection = arrayOf(
                        PhoneLookupContract.TYPE,
                        PhoneLookupContract.LABEL,
                        PhoneLookupContract.PHOTO_URI,
                        PhoneLookupContract.CONTACT_ID,
                        PhoneLookupContract.DISPLAY_NAME,
                    ),
                ),
            ) { row ->
                PhoneLookup(
                    contactId = row.long(PhoneLookupContract.CONTACT_ID),
                    name = row.string(PhoneLookupContract.DISPLAY_NAME),
                    label = row.string(PhoneLookupContract.LABEL),
                    type = row.int(PhoneLookupContract.TYPE),
                    photoUri = row.string(PhoneLookupContract.PHOTO_URI),
                )
            }.firstOrNull()
        }.onFailure { Log.e(TAG, "Contact lookup failed", it) }.getOrNull()
    }

    private companion object {
        const val TAG = "PhoneRepository"
    }
}
