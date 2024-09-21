package com.chooloo.www.chooloolib.domain.contentresolver

import android.content.ContentResolver
import android.database.ContentObserver
import android.database.Cursor
import android.net.Uri
import com.pushtorefresh.storio3.contentresolver.queries.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

abstract class BaseContentResolver<ItemType>(
    filter: String? = null,
    private val contentResolver: ContentResolver
) {
    protected var currentFilter: String? = null

    private val _query: Query
        get() = Query.builder().uri(finalUri).columns(*projection)
            .whereArgs(*(selectionArgs ?: arrayOf()))
            .where(if (selection == "") null else selection)
            .sortOrder(if (sortOrder == "") null else sortOrder).build()

    private val finalUri: Uri
        get() = if (filterUri != null && currentFilter?.isNotEmpty() == true) {
            Uri.withAppendedPath(filterUri, currentFilter)
        } else {
            uri
        }

    init {
        currentFilter = if (filter == "") null else filter
    }

    fun queryCursor() =
        contentResolver.query(
            finalUri,
            projection,
            if (selection == "") null else selection,
            selectionArgs ?: arrayOf(),
            if (sortOrder == "") null else sortOrder
        )

    fun getItems() = convertCursorToItems(queryCursor())

    private fun getCursorFlow() = callbackFlow {
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
        getCursorFlow().collect { emit(convertCursorToItems(it)) }
    }

    private fun convertCursorToItems(cursor: Cursor?): List<ItemType> {
        val content = ArrayList<ItemType>()
        while (cursor != null && cursor.moveToNext() && !cursor.isClosed) {
            content.add(convertCursorToItem(cursor))
        }
        cursor?.close()
        return content.toList()
    }

    abstract val uri: Uri
    abstract val filterUri: Uri?
    abstract val selection: String?
    abstract val sortOrder: String?
    abstract val projection: Array<String>
    abstract val selectionArgs: Array<String>?
    abstract fun convertCursorToItem(cursor: Cursor): ItemType
}