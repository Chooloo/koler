package com.chooloo.www.koler.contentresolver

import android.content.ContentResolver
import android.content.Context
import android.database.ContentObserver
import android.database.Cursor
import android.net.Uri

abstract class BaseContentResolver<T>(
        private val context: Context,
) : ContentResolver(context) {
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

    private fun queryContent() = context.contentResolver.query(
            onGetUri(),
            onGetProjection(),
            onGetSelection(),
            onGetSelectionArgs(),
            onGetSortOrder()
    )

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
    abstract fun onGetProjection(): Array<String>?
    abstract fun convertCursorToContent(cursor: Cursor?): T
}