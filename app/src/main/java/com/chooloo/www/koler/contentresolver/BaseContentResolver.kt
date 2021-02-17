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
    private var _observer: ContentObserver
    private var _onContentChangedListener: ((T?) -> Unit?)? = null

    val content: T
        get() = convertCursorToContent(queryContent())

    open val requiredPermissions: Array<String>
        get() = arrayOf()

    init {
        _observer = object : ContentObserver(null) {
            override fun onChange(selfChange: Boolean) {
                _onContentChangedListener?.invoke(content)
            }
        }
    }

    private fun getUri(): Uri {
        return if (_filter != null) {
            Uri.withAppendedPath(onGetFilterUri(), _filter)
        } else {
            onGetUri()
        }
    }

    private fun queryContent() = context.contentResolver.query(
            getUri(),
            onGetProjection(),
            onGetSelection(),
            onGetSelectionArgs(),
            onGetSortOrder()
    )

    fun setFilter(filter: String?) {
        _filter = if (filter == "") null else filter
    }

    fun observe() {
        registerContentObserver(onGetUri(), true, _observer)
    }

    fun detach() {
        unregisterContentObserver(_observer)
    }

    fun setOnContentChangedListener(onContentChangedListener: ((T?) -> Unit?)?) {
        _onContentChangedListener = onContentChangedListener
    }

    open fun onGetSelection(): String? = null
    open fun onGetSortOrder(): String? = null
    open fun onGetSelectionArgs(): Array<String>? = null

    abstract fun onGetUri(): Uri
    abstract fun onGetFilterUri(): Uri
    abstract fun onGetProjection(): Array<String>?
    abstract fun convertCursorToContent(cursor: Cursor?): T

    open inner class SelectionBuilder {
        private val selections = arrayListOf<String>()

        fun addSelection(key: String, value: Any?): SelectionBuilder {
            value?.let { selections.add("$key = $value") }
            return this
        }

        fun build(): String {
            return selections.joinToString(separator = " AND ")
        }
    }
}