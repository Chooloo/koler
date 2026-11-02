package com.chooloo.www.chooloolib.data.repository

import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.provider.CallLog.Calls
import android.telephony.PhoneNumberUtils
import com.chooloo.www.chooloolib.data.source.ContentQuery
import com.chooloo.www.chooloolib.data.source.int
import com.chooloo.www.chooloolib.data.source.long
import com.chooloo.www.chooloolib.data.source.observeAll
import com.chooloo.www.chooloolib.data.source.string
import com.chooloo.www.chooloolib.di.IoDispatcher
import com.chooloo.www.chooloolib.domain.model.Recent
import com.chooloo.www.chooloolib.domain.repository.PhoneRepository
import com.chooloo.www.chooloolib.domain.repository.RecentRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.withContext

@OptIn(ExperimentalCoroutinesApi::class)
@Singleton
class RecentRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val contentResolver: ContentResolver,
    private val phoneRepository: PhoneRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : RecentRepository {
    override fun recents(filter: String?): Flow<List<Recent>> {
        val pattern = filter?.takeIf { it.isNotEmpty() }?.let { "%$it%" }
        return contentResolver.observeAll(
            ContentQuery(
                uri = Calls.CONTENT_URI,
                projection = PROJECTION,
                selection = pattern?.let { "(${Calls.CACHED_NAME} LIKE ? OR ${Calls.NUMBER} LIKE ?)" },
                selectionArgs = pattern?.let { arrayOf(it, it) },
                sortOrder = "${Calls.DATE} DESC",
            ),
            ::toRecent
        ).mapLatest(::withContactDetails).flowOn(ioDispatcher)
    }

    override fun recent(recentId: Long): Flow<Recent?> =
        contentResolver.observeAll(
            ContentQuery(
                uri = Calls.CONTENT_URI,
                projection = PROJECTION,
                selection = "${Calls._ID} = ?",
                selectionArgs = arrayOf(recentId.toString()),
            ),
            ::toRecent
        ).map { it.firstOrNull() }.flowOn(ioDispatcher)

    override fun recentsOfNumbers(numbers: List<String>): Flow<List<Recent>> {
        val suffixes = numbers.map { number -> number.filter(Char::isDigit).takeLast(MATCH_DIGITS) }.filter { it.isNotEmpty() }
        if (suffixes.isEmpty()) return flowOf(emptyList())
        return contentResolver.observeAll(
            ContentQuery(
                uri = Calls.CONTENT_URI,
                projection = PROJECTION,
                selection = suffixes.joinToString(" OR ") { "${Calls.NUMBER} LIKE ?" },
                selectionArgs = suffixes.map { "%$it" }.toTypedArray(),
                sortOrder = "${Calls.DATE} DESC",
            ),
            ::toRecent
        )
            .map { recents -> recents.filter { recent -> numbers.any { PhoneNumberUtils.compare(context, it, recent.number) } } }
            .flowOn(ioDispatcher)
    }

    override suspend fun lastOutgoingNumber(): String? = withContext(ioDispatcher) {
        Calls.getLastOutgoingCall(context)?.takeIf { it.isNotEmpty() }
    }

    override suspend fun deleteRecent(recentId: Long) = withContext(ioDispatcher) {
        val deleted = contentResolver.delete(ContentUris.withAppendedId(Calls.CONTENT_URI, recentId), null, null)
        check(deleted > 0) { "Recent $recentId was not deleted" }
    }

    override suspend fun deleteAllRecents() = withContext(ioDispatcher) {
        contentResolver.delete(Calls.CONTENT_URI, null, null)
        Unit
    }

    private suspend fun withContactDetails(recents: List<Recent>): List<Recent> {
        val lookups = recents.filter { it.name == null && it.number.isNotEmpty() }
            .map { it.number }
            .distinct()
            .associateWith { phoneRepository.lookup(it) }
        return recents.map { recent ->
            val lookup = lookups[recent.number] ?: return@map recent
            recent.copy(
                name = lookup.name,
                photoUri = lookup.photoUri ?: recent.photoUri,
                numberType = lookup.type,
                numberLabel = lookup.label,
            )
        }
    }

    private fun toRecent(cursor: Cursor) = Recent(
        id = cursor.long(Calls._ID),
        number = cursor.string(Calls.NUMBER).orEmpty(),
        dateMillis = cursor.long(Calls.DATE),
        type = typeOf(cursor.int(Calls.TYPE)),
        durationSeconds = cursor.long(Calls.DURATION),
        name = cursor.string(Calls.CACHED_NAME)?.takeIf { it.isNotEmpty() },
        photoUri = cursor.string(Calls.CACHED_PHOTO_URI),
        numberType = cursor.int(Calls.CACHED_NUMBER_TYPE).takeIf { it != 0 },
        numberLabel = cursor.string(Calls.CACHED_NUMBER_LABEL),
        presentation = presentationOf(cursor.int(Calls.NUMBER_PRESENTATION)),
    )

    private fun typeOf(type: Int) = when (type) {
        Calls.INCOMING_TYPE, Calls.ANSWERED_EXTERNALLY_TYPE -> Recent.Type.INCOMING
        Calls.OUTGOING_TYPE -> Recent.Type.OUTGOING
        Calls.MISSED_TYPE -> Recent.Type.MISSED
        Calls.VOICEMAIL_TYPE -> Recent.Type.VOICEMAIL
        Calls.REJECTED_TYPE -> Recent.Type.REJECTED
        Calls.BLOCKED_TYPE -> Recent.Type.BLOCKED
        else -> Recent.Type.UNKNOWN
    }

    private fun presentationOf(presentation: Int) = when (presentation) {
        Calls.PRESENTATION_RESTRICTED -> Recent.Presentation.RESTRICTED
        Calls.PRESENTATION_PAYPHONE -> Recent.Presentation.PAYPHONE
        Calls.PRESENTATION_UNKNOWN -> Recent.Presentation.UNKNOWN
        else -> Recent.Presentation.ALLOWED
    }

    private companion object {
        const val MATCH_DIGITS = 7
        val PROJECTION = arrayOf(
            Calls._ID,
            Calls.NUMBER,
            Calls.NUMBER_PRESENTATION,
            Calls.DATE,
            Calls.DURATION,
            Calls.CACHED_NAME,
            Calls.CACHED_PHOTO_URI,
            Calls.CACHED_NUMBER_TYPE,
            Calls.CACHED_NUMBER_LABEL,
            Calls.TYPE,
        )
    }
}
