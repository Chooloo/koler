package com.chooloo.www.chooloolib.data.source

import android.content.ContentResolver
import android.database.ContentObserver
import javax.inject.Inject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.map

class ContentSource @Inject constructor(private val contentResolver: ContentResolver) {
    fun <T> queryAll(query: ContentQuery, map: (CursorRow) -> T): List<T> {
        val cursor = contentResolver.query(
            query.uri,
            query.projection,
            query.selection,
            query.selectionArgs,
            query.sortOrder,
        ) ?: throw ContentException.ProviderUnavailable(query.uri)
        return cursor.use {
            val row = CursorRow(it)
            buildList { while (it.moveToNext()) add(map(row)) }
        }
    }

    fun <T> observeAll(query: ContentQuery, map: (CursorRow) -> T): Flow<List<T>> = callbackFlow {
        val observer = object : ContentObserver(null) {
            override fun onChange(selfChange: Boolean) {
                trySend(Unit)
            }
        }
        contentResolver.registerContentObserver(query.uri, true, observer)
        trySend(Unit)
        awaitClose { contentResolver.unregisterContentObserver(observer) }
    }.conflate().map { queryAll(query, map) }
}
