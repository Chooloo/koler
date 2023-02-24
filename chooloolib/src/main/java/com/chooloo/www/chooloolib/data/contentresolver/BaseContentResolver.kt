package com.chooloo.www.chooloolib.data.contentresolver

import android.content.ContentResolver
import android.database.ContentObserver
import android.database.Cursor
import android.net.Uri
import com.chooloo.www.chooloolib.di.module.IoDispatcher
import com.pushtorefresh.storio3.contentresolver.queries.Query
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class BaseContentResolver<ItemType>(
    private val contentResolver: ContentResolver,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {
    private var _filter: String? = null

    private val _query: Query
        get() = Query.builder()
            .uri(finalUri)
            .columns(*projection)
            .whereArgs(*(selectionArgs ?: arrayOf()))
            .where(if (selection == "") null else selection)
            .sortOrder(if (sortOrder == "") null else sortOrder)
            .build()

    val finalUri: Uri
        get() = if (filterUri != null && _filter?.isNotEmpty() == true) {
            Uri.withAppendedPath(filterUri, _filter)
        } else {
            uri
        }

    open var filter: String?
        get() = _filter
        set(value) {
            _filter = if (value == "") null else value
        }

    suspend fun queryCursor() = withContext(ioDispatcher) {
        contentResolver.query(
            finalUri,
            projection,
            if (selection == "") null else selection,
            selectionArgs ?: arrayOf(),
            if (sortOrder == "") null else sortOrder
        )
    }

    suspend fun queryItems() = convertCursorToItems(queryCursor())

    fun getCursorFlow() = callbackFlow {
        val callback = object : ContentObserver(null) {
            override fun onChange(selfChange: Boolean) {
                launch {
                    send(queryCursor())
                }
            }
        }
        send(queryCursor())
        contentResolver.registerContentObserver(finalUri, false, callback)
        awaitClose { contentResolver.unregisterContentObserver(callback) }
    }

    fun getItemsFlow() = flow {
        getCursorFlow().collect {
            emit(convertCursorToItems(it))
        }
    }

    private fun convertCursorToItems(cursor: Cursor?): ArrayList<ItemType> {
        val content = ArrayList<ItemType>()
        while (cursor != null && cursor.moveToNext() && !cursor.isClosed) {
            content.add(convertCursorToItem(cursor))
        }
        cursor?.close()
        return content
    }

    abstract val uri: Uri
    abstract val filterUri: Uri?
    abstract val selection: String?
    abstract val sortOrder: String?
    abstract val projection: Array<String>
    abstract val selectionArgs: Array<String>?
    abstract fun convertCursorToItem(cursor: Cursor): ItemType
}