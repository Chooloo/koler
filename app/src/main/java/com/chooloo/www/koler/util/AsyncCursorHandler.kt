package com.chooloo.www.koler.util

import android.content.AsyncQueryHandler
import android.content.ContentResolver
import android.database.Cursor
import java.lang.ref.WeakReference

class AsyncCursorHandler : AsyncQueryHandler {
    private var _listener: WeakReference<AsyncQueryListener>? = null

    interface AsyncQueryListener {
        fun onQueryComplete(token: Int, cookie: Any?, cursor: Cursor?)
    }

    constructor(cr: ContentResolver, listener: AsyncQueryListener) : super(cr) {
        _listener = WeakReference<AsyncQueryListener>(listener)
    }

    constructor(cr: ContentResolver) : super(cr)

    /**
     * Assign the given [AsyncQueryListener] to receive query events from
     * asynchronous calls. Will replace any existing listener.
     */
    fun setQueryListener(listener: AsyncQueryListener?) {
        _listener = WeakReference<AsyncQueryListener>(listener)
    }

    /** {@inheritDoc}  */
    override fun onQueryComplete(token: Int, cookie: Any?, cursor: Cursor?) {
        val listener: AsyncQueryListener? = _listener?.get()
        if (listener != null) {
            listener.onQueryComplete(token, cookie, cursor)
        } else cursor?.close()
    }
}