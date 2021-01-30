package com.chooloo.www.callmanager.livedata

import android.content.Context
import android.database.ContentObserver
import android.database.Cursor
import android.net.Uri
import androidx.core.content.ContentResolverCompat
import androidx.core.os.CancellationSignal
import androidx.lifecycle.LiveData

abstract class CursorLiveData(
        private val _context: Context
) : LiveData<Cursor>() {

    private lateinit var _observer: CursorContentObserver

    override fun onActive() {
        loadCursor()
        _observer = CursorContentObserver()
        _context.contentResolver.registerContentObserver(onGetUri(), true, _observer)
    }

    override fun onInactive() {
        _context.contentResolver.unregisterContentObserver(_observer)
    }

    protected fun loadCursor() {
        postValue(
                ContentResolverCompat.query(
                        _context.contentResolver,
                        onGetUri(),
                        onGetProjection(),
                        onGetSelection(),
                        onGetSelectionArgs(),
                        onGetSortOrder(),
                        CancellationSignal()
                )
        )
    }

    abstract fun onGetUri(): Uri
    abstract fun onGetProjection(): Array<String>?
    abstract fun onGetSelection(): String?
    abstract fun onGetSelectionArgs(): Array<String>?
    abstract fun onGetSortOrder(): String?

    inner class CursorContentObserver : ContentObserver(null) {
        override fun onChange(selfChange: Boolean) {
            loadCursor()
        }
    }
}
