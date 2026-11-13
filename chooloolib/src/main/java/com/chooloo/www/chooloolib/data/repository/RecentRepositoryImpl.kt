package com.chooloo.www.chooloolib.data.repository

import android.Manifest.permission.WRITE_CALL_LOG
import android.content.ContentResolver
import android.content.Context
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.provider.CallLog.Calls
import android.telephony.PhoneNumberUtils
import android.util.Log
import androidx.core.content.contentValuesOf
import com.chooloo.www.chooloolib.data.source.ContentQuery
import com.chooloo.www.chooloolib.data.source.ContentSource
import com.chooloo.www.chooloolib.di.IoDispatcher
import com.chooloo.www.chooloolib.domain.enum.RecentPresentation
import com.chooloo.www.chooloolib.domain.enum.RecentType
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
    private val contentSource: ContentSource,
    private val phoneRepository: PhoneRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : RecentRepository {
    override fun recents(filter: String): Flow<List<Recent>> {
        val pattern = filter.takeIf { it.isNotEmpty() }?.let { "%$it%" }
        return observeRecents(
            selection = pattern?.let { "(${Calls.CACHED_NAME} LIKE ? OR ${Calls.NUMBER} LIKE ?)" },
            selectionArgs = pattern?.let { arrayOf(it, it) },
        ).mapLatest { recents ->
            val lookups = recents.filter { it.name == null && it.number.isNotEmpty() }
                .map { it.number }
                .distinct()
                .associateWith { phoneRepository.lookup(it) }
            recents.map { recent ->
                val lookup = lookups[recent.number] ?: return@map recent
                recent.copy(
                    name = lookup.name,
                    photoUri = lookup.photoUri ?: recent.photoUri,
                    numberType = lookup.type,
                    numberLabel = lookup.label,
                )
            }
        }.flowOn(ioDispatcher)
    }

    override fun recent(recentId: Long): Flow<Recent?> =
        observeRecents("${Calls._ID} = ?", arrayOf(recentId.toString()))
            .map { it.firstOrNull() }
            .flowOn(ioDispatcher)

    override fun recentsOfNumbers(numbers: List<String>): Flow<List<Recent>> {
        val suffixes = numbers.map { it.filter(Char::isDigit).takeLast(MATCH_DIGITS) }
            .filter { it.isNotEmpty() }
        if (suffixes.isEmpty()) return flowOf(emptyList())
        return observeRecents(
            selection = suffixes.joinToString(" OR ") { "${Calls.NUMBER} LIKE ?" },
            selectionArgs = suffixes.map { "%$it" }.toTypedArray(),
        )
            .map { recents ->
                recents.filter { recent ->
                    numbers.any { PhoneNumberUtils.compare(context, it, recent.number) }
                }
            }
            .flowOn(ioDispatcher)
    }

    override suspend fun lastOutgoingNumber(): String? = withContext(ioDispatcher) {
        Calls.getLastOutgoingCall(context)?.takeIf { it.isNotEmpty() }
    }

    override suspend fun deleteAllRecents() {
        withContext(ioDispatcher) { contentResolver.delete(Calls.CONTENT_URI, null, null) }
    }

    override suspend fun markMissedCallsRead() {
        if (context.checkSelfPermission(WRITE_CALL_LOG) != PERMISSION_GRANTED) {
            Log.e(TAG, "Missed calls stay unread: WRITE_CALL_LOG is not granted")
            return
        }
        withContext(ioDispatcher) {
            contentResolver.update(
                Calls.CONTENT_URI,
                contentValuesOf(Calls.NEW to 0, Calls.IS_READ to 1),
                "${Calls.TYPE} = ? AND ${Calls.NEW} = 1",
                arrayOf(Calls.MISSED_TYPE.toString()),
            )
        }
    }

    private fun observeRecents(selection: String?, selectionArgs: Array<String>?) =
        contentSource.observeAll(
            ContentQuery(
                uri = Calls.CONTENT_URI,
                projection = PROJECTION,
                selection = selection,
                selectionArgs = selectionArgs,
                sortOrder = "${Calls.DATE} DESC",
            ),
        ) { row ->
            Recent(
                id = row.long(Calls._ID),
                number = row.string(Calls.NUMBER).orEmpty(),
                dateMillis = row.long(Calls.DATE),
                type = when (row.int(Calls.TYPE)) {
                    Calls.INCOMING_TYPE, Calls.ANSWERED_EXTERNALLY_TYPE -> RecentType.INCOMING
                    Calls.OUTGOING_TYPE -> RecentType.OUTGOING
                    Calls.MISSED_TYPE -> RecentType.MISSED
                    Calls.VOICEMAIL_TYPE -> RecentType.VOICEMAIL
                    Calls.REJECTED_TYPE -> RecentType.REJECTED
                    Calls.BLOCKED_TYPE -> RecentType.BLOCKED
                    else -> RecentType.UNKNOWN
                },
                durationSeconds = row.long(Calls.DURATION),
                name = row.string(Calls.CACHED_NAME)?.takeIf { it.isNotEmpty() },
                photoUri = row.string(Calls.CACHED_PHOTO_URI),
                numberType = row.int(Calls.CACHED_NUMBER_TYPE).takeIf { it != 0 },
                numberLabel = row.string(Calls.CACHED_NUMBER_LABEL),
                presentation = when (row.int(Calls.NUMBER_PRESENTATION)) {
                    Calls.PRESENTATION_RESTRICTED -> RecentPresentation.RESTRICTED
                    Calls.PRESENTATION_PAYPHONE -> RecentPresentation.PAYPHONE
                    Calls.PRESENTATION_UNKNOWN -> RecentPresentation.UNKNOWN
                    else -> RecentPresentation.ALLOWED
                },
            )
        }

    private companion object {
        const val TAG = "RecentRepository"
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
