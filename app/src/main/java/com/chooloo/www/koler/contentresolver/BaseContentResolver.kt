package com.chooloo.www.koler.contentresolver

import android.content.ContentResolver
import android.content.Context
import android.database.ContentObserver
import android.database.Cursor
import android.net.Uri

abstract class BaseContentResolver<T>(private val context: Context) : ContentResolver(context) {
    private var _filter: String? = null
    private var _observer: ContentObserver? = null
    private var _onContentChangedListener: ((T?) -> Unit?)? = null


    val content: T
        get() = convertCursorToContent(queryContent())

    var filter: String?
        get() = _filter
        set(value) {
            _filter = if (value == "") null else value
        }


    private fun chooseUri(): Uri {
        return if (filterUri != null && _filter?.isNotEmpty() == true) {
            Uri.withAppendedPath(filterUri, _filter)
        } else {
            uri
        }
    }

    private fun queryContent(): Cursor? {
        return context.contentResolver.query(
            chooseUri(),
            projection,
            selection,
            selectionArgs,
            sortOrder
        )
    }


    fun observe() {
        _observer = object : ContentObserver(null) {
            override fun onChange(selfChange: Boolean) {
                _onContentChangedListener?.invoke(content)
            }
        }
        _observer?.let { registerContentObserver(uri, true, it) }
    }

    fun detach() {
        _observer?.let { unregisterContentObserver(it) }
        _observer = null
    }

    fun setOnContentChangedListener(onContentChangedListener: (T?) -> Unit? = { _ -> }) {
        _onContentChangedListener = onContentChangedListener
    }


    //region base resolver data abstract getters

    abstract val uri: Uri
    abstract val filterUri: Uri?
    abstract val selection: String?
    abstract val sortOrder: String?
    abstract val projection: Array<String>?
    abstract val selectionArgs: Array<String>?
    abstract fun convertCursorToContent(cursor: Cursor?): T

    //endregion

    
    open inner class SelectionBuilder {
        private val selections = arrayListOf<String>()

        fun addSelection(key: String, value: Any?) = this.also {
            value?.let { selections.add("$key = $value") }
        }

        fun addNotNull(key: String) = this.also {
            selections.add("$key IS NOT NULL")
        }

        fun addString(string: String) = this.also {
            selections.add(string)
        }

        fun build() = selections.joinToString(" AND ")
    }
}