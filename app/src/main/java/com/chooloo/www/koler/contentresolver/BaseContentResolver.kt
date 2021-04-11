package com.chooloo.www.koler.contentresolver

import android.content.ContentResolver
import android.content.Context
import android.database.ContentObserver
import android.database.Cursor
import android.net.Uri

abstract class BaseContentResolver<T>(
    private val context: Context,
) : ContentResolver(context) {
    private var _filter: String? = null
    private var _onContentChangedListener: ((T?) -> Unit?)? = null
    private val _observer by lazy {
        object : ContentObserver(null) {
            override fun onChange(selfChange: Boolean) {
                _onContentChangedListener?.invoke(content)
            }
        }
    }

    val content: T
        get() = convertCursorToContent(queryContent())

    var filter: String?
        get() = _filter
        set(value) {
            _filter = if (value == "") null else value
        }

    open val requiredPermissions: Array<String>
        get() = arrayOf()

    protected fun chooseUri() = if (filterUri != null && _filter?.isNotEmpty() == true) {
        Uri.withAppendedPath(filterUri, _filter)
    } else {
        uri
    }

    private fun queryContent() = context.contentResolver.query(
        chooseUri(),
        projection,
        selection,
        selectionArgs,
        sortOrder
    )

    fun observe() {
        registerContentObserver(uri, true, _observer)
    }

    fun detach() {
        unregisterContentObserver(_observer)
    }

    fun setOnContentChangedListener(onContentChangedListener: (T?) -> Unit? = { _ -> }) {
        _onContentChangedListener = onContentChangedListener
    }

    abstract val uri: Uri
    abstract val filterUri: Uri?
    abstract val selection: String?
    abstract val sortOrder: String?
    abstract val projection: Array<String>?
    abstract val selectionArgs: Array<String>?
    abstract fun convertCursorToContent(cursor: Cursor?): T

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