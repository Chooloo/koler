package com.chooloo.www.koler.contentresolver

import android.content.ContentResolver
import android.content.Context
import android.database.ContentObserver
import android.database.Cursor
import android.net.Uri
import android.widget.Toast

abstract class BaseContentResolver<T>(
        private val context: Context,
) : ContentResolver(context) {
    private var _currentUri: Uri
    private var _observer: ContentObserver
    private var _onContentChangedListener: ((T?) -> Unit?)? = null

    val content: T
        get() = convertCursorToContent(queryContent())

    open val requiredPermissions: Array<String>
        get() = arrayOf()

    init {
        _currentUri = onGetDefaultUri()
        _observer = object : ContentObserver(null) {
            override fun onChange(selfChange: Boolean) {
                _onContentChangedListener?.invoke(content)
            }
        }
    }

    fun queryContent(): Cursor? {
        return context.contentResolver.query(
                _currentUri,
                onGetProjection(),
                onGetSelection(),
                onGetSelectionArgs(),
                onGetSortOrder()
        )
    }

    fun reset() {
        _currentUri = onGetDefaultUri()
    }

    fun filter(filterString: String) {
        onGetFilterUri().also {
            if (it != null) {
                _currentUri = Uri.withAppendedPath(it, filterString).buildUpon().build()
                detach()
                observe()
            } else {
                Toast.makeText(context, "Cannot filter this type of content", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun observe() {
        registerContentObserver(_currentUri, true, _observer)
    }

    fun detach() {
        unregisterContentObserver(_observer)
    }

    fun setOnContentChangedListener(onContentChangedListener: ((T?) -> Unit?)?) {
        _onContentChangedListener = onContentChangedListener
    }

    abstract fun onGetDefaultUri(): Uri
    abstract fun onGetFilterUri(): Uri?
    abstract fun onGetSelection(): String?
    abstract fun onGetSortOrder(): String?
    abstract fun onGetSelectionArgs(): Array<String>?
    abstract fun onGetProjection(): Array<String>?
    abstract fun convertCursorToContent(cursor: Cursor?): T
}