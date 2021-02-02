package com.chooloo.www.callmanager.contentresolver

import android.content.ContentResolver
import android.content.Context
import android.database.ContentObserver
import android.net.Uri

abstract class BaseContentResolver<T>(
        val context: Context,
        val uri: Uri
) : ContentResolver(context) {

    private var _observer: ContentObserver
    private var _onContentChangedListener: ((T) -> Unit?)? = null

    init {
        _observer = object : ContentObserver(null) {
            override fun onChange(selfChange: Boolean) {
                _onContentChangedListener?.invoke(getContent())
            }
        }
    }

    fun observe() {
        registerContentObserver(uri, true, _observer)
    }

    fun detach() {
        unregisterContentObserver(_observer)
    }

    fun setOnContentChangedListener(onContentChangedListener: (T) -> Unit) {
        _onContentChangedListener = onContentChangedListener
    }

    abstract fun getContent(): T
}