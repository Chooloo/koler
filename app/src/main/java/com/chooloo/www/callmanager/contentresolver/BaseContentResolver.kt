package com.chooloo.www.callmanager.contentresolver

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

    private var _observer: ContentObserver
    private var _onContentChangedListener: ((T?) -> Unit?)? = null
    private var _uri: Uri

    init {
        _observer = object : ContentObserver(null) {
            override fun onChange(selfChange: Boolean) {
                _onContentChangedListener?.invoke(getContent())
            }
        }
        _uri = defaultUri
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
        filterUri.let { _uri = Uri.withAppendedPath(filterUri, filterString).buildUpon().build() }
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