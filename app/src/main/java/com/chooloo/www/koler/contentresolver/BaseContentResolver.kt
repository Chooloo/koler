package com.chooloo.www.koler.contentresolver

import android.content.ContentResolver
import android.content.Context
import android.database.ContentObserver
import android.database.Cursor
import android.net.Uri

open class BaseContentResolver<T>(
        private val context: Context,
        var defaultUri: Uri,
        var filterUri: Uri? = null,
        var projection: Array<String>? = null,
        var selection: String? = null,
        var selectionArgs: Array<String>? = null,
        var sortOrder: String? = null
) : ContentResolver(context) {

    private var _uri: Uri
    private var _observer: ContentObserver
    private var _onContentChangedListener: ((T?) -> Unit?)? = null

    init {
        _uri = defaultUri
        _observer = object : ContentObserver(null) {
            override fun onChange(selfChange: Boolean) {
                _onContentChangedListener?.invoke(getContent())
            }
        }
    }

    fun queryContent(): Cursor? {
        return context.contentResolver.query(
                defaultUri,
                projection,
                selection,
                selectionArgs,
                sortOrder
        )
    }

    fun reset() {
        _uri = defaultUri
    }

    fun filter(filterString: String) {
        if (filterUri != null) {
            _uri = Uri.withAppendedPath(filterUri, filterString).buildUpon().build()
        } else {
            TODO("filter uri not given")
        }
    }

    fun observe() {
        registerContentObserver(defaultUri, true, _observer)
    }

    fun detach() {
        unregisterContentObserver(_observer)
    }

    fun setOnContentChangedListener(onContentChangedListener: ((T?) -> Unit?)?) {
        _onContentChangedListener = onContentChangedListener
    }

    open fun getContent(): T? = null
}