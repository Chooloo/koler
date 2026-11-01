package com.chooloo.www.chooloolib.data.contentresolver

import android.content.ContentResolver
import android.database.ContentObserver
import android.database.Cursor
import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

abstract class BaseContentResolver<ItemType>(
    filter: String? = null,
    private val contentResolver: ContentResolver
) {
    protected val currentFilter: String? = filter?.takeIf { it.isNotEmpty() }

    private val finalUri: Uri
        get() = if (filterUri != null && !currentFilter.isNullOrEmpty()) {
            Uri.withAppendedPath(filterUri, Uri.encode(currentFilter))
        } else {
            uri
        }

    private fun queryItems(): List<ItemType> = try {
        contentResolver.query(
            finalUri,
            projection,
            selection?.takeIf { it.isNotEmpty() },
            selectionArgs,
            sortOrder?.takeIf { it.isNotEmpty() }
        )?.use { cursor ->
            buildList {
                while (cursor.moveToNext()) {
                    add(convertCursorToItem(cursor))
                }
            }
        } ?: emptyList()
    } catch (_: SecurityException) {
        emptyList()
    }

    suspend fun getItems(): List<ItemType> = withContext(Dispatchers.IO) {
        queryItems()
    }

    fun getItemsFlow(): Flow<List<ItemType>> = callbackFlow {
        val observer = object : ContentObserver(null) {
            override fun onChange(selfChange: Boolean) {
                trySend(Unit)
            }
        }
        trySend(Unit)
        try {
            contentResolver.registerContentObserver(finalUri, true, observer)
        } catch (_: SecurityException) {
            close()
            return@callbackFlow
        }
        awaitClose { contentResolver.unregisterContentObserver(observer) }
    }.conflate()
        .map { queryItems() }
        .flowOn(Dispatchers.IO)

    abstract val uri: Uri
    abstract val filterUri: Uri?
    abstract val selection: String?
    abstract val sortOrder: String?
    abstract val projection: Array<String>
    abstract val selectionArgs: Array<String>?
    abstract fun convertCursorToItem(cursor: Cursor): ItemType
}
