package com.chooloo.www.chooloolib.data.source

import android.content.ContentResolver
import android.database.ContentObserver
import android.database.Cursor
import android.net.Uri
import androidx.core.database.getIntOrNull
import androidx.core.database.getLongOrNull
import androidx.core.database.getStringOrNull
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.map

class ContentQuery(
    val uri: Uri,
    val projection: Array<String>,
    val selection: String? = null,
    val selectionArgs: Array<String>? = null,
    val sortOrder: String? = null,
)

fun <T> ContentResolver.queryAll(query: ContentQuery, map: (Cursor) -> T): List<T> {
    val cursor = query(query.uri, query.projection, query.selection, query.selectionArgs, query.sortOrder)
        ?: error("Content provider for ${query.uri} is unavailable")
    return cursor.use { buildList { while (it.moveToNext()) add(map(it)) } }
}

fun <T> ContentResolver.observeAll(query: ContentQuery, map: (Cursor) -> T): Flow<List<T>> =
    callbackFlow {
        val observer = object : ContentObserver(null) {
            override fun onChange(selfChange: Boolean) {
                trySend(Unit)
            }
        }
        registerContentObserver(query.uri, true, observer)
        trySend(Unit)
        awaitClose { unregisterContentObserver(observer) }
    }.conflate().map { queryAll(query, map) }

fun Cursor.string(column: String): String? = getStringOrNull(getColumnIndexOrThrow(column))

fun Cursor.long(column: String): Long = getLongOrNull(getColumnIndexOrThrow(column)) ?: 0L

fun Cursor.int(column: String): Int = getIntOrNull(getColumnIndexOrThrow(column)) ?: 0

fun Uri.withFilter(filterUri: Uri, filter: String?): Uri =
    if (filter.isNullOrEmpty()) this else Uri.withAppendedPath(filterUri, Uri.encode(filter))
