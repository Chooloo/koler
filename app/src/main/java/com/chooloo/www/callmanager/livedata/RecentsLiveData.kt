package com.chooloo.www.callmanager.livedata

import android.content.Context
import android.database.ContentObserver
import android.database.Cursor
import android.provider.CallLog
import androidx.core.content.ContentResolverCompat
import androidx.core.os.CancellationSignal
import androidx.lifecycle.LiveData
import com.chooloo.www.callmanager.entity.Recent
import java.util.*
import kotlin.collections.ArrayList

class RecentsLiveData(
        private val context: Context
) : LiveData<Array<Recent>>() {
    companion object {
        private const val COLUMN_ID = CallLog.Calls._ID
        private const val COLUMN_NUMBER = CallLog.Calls.NUMBER
        private const val COLUMN_PRESENTATION = CallLog.Calls.NUMBER_PRESENTATION
        private const val COLUMN_DATE = CallLog.Calls.DATE
        private const val COLUMN_DURATION = CallLog.Calls.DURATION
        private const val COLUMN_TYPE = CallLog.Calls.TYPE
        private const val COLUMN_CACHED_NAME = CallLog.Calls.CACHED_NAME

        private const val RECENTS_SORT_ORDER = "$COLUMN_DATE DESC"
        private val RECENTS_URI = CallLog.Calls.CONTENT_URI
        private val RECENTS_SELECTION = null
        private val RECENTS_SELECTION_ARGS = null
        private val RECENTS_PROJECTION = arrayOf(
                COLUMN_ID,
                COLUMN_NUMBER,
                COLUMN_DATE,
                COLUMN_DURATION,
                COLUMN_TYPE,
                COLUMN_CACHED_NAME,
                COLUMN_PRESENTATION
        )
    }

    private lateinit var _observer: CursorContentObserver

    override fun onActive() {
        _observer = CursorContentObserver()
        context.contentResolver.registerContentObserver(RECENTS_URI, true, _observer)
    }

    override fun onInactive() {
        context.contentResolver.unregisterContentObserver(_observer)
    }

    private fun onRecentsCursorChanged() {
        postValue(getRecents())
    }

    private fun getRecents(): Array<Recent> {
        val recents: MutableList<Recent> = ArrayList()
        val cursor = getRecentsCursor()
        while (cursor.moveToNext()) {
            recents.add(Recent(
                    callId = cursor.getLong(cursor.getColumnIndex(COLUMN_ID)),
                    callerNumber = cursor.getString(cursor.getColumnIndex(COLUMN_NUMBER)),
                    callDuration = cursor.getString(cursor.getColumnIndex(COLUMN_DURATION)),
                    callDate = Date(cursor.getLong(cursor.getColumnIndex(COLUMN_DATE))),
                    callType = cursor.getInt(cursor.getColumnIndex(COLUMN_TYPE))))
        }
        return recents.toTypedArray()
    }

    private fun getRecentsCursor(): Cursor {
        return ContentResolverCompat.query(
                context.contentResolver,
                RECENTS_URI,
                RECENTS_PROJECTION,
                RECENTS_SELECTION,
                RECENTS_SELECTION_ARGS,
                RECENTS_SORT_ORDER,
                CancellationSignal()
        )
    }

    inner class CursorContentObserver : ContentObserver(null) {
        override fun onChange(selfChange: Boolean) {
            onRecentsCursorChanged()
        }
    }
}