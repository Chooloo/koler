package com.chooloo.www.callmanager.contentresolver

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.CallLog
import androidx.core.content.ContentResolverCompat
import androidx.core.os.CancellationSignal
import com.chooloo.www.callmanager.entity.Recent
import java.util.*
import kotlin.collections.ArrayList

class RecentsContentResolver(context: Context) : BaseContentResolver<Array<Recent>>(
        context = context,
        uri = URI
) {

    companion object {
        private const val COLUMN_ID = CallLog.Calls._ID
        private const val COLUMN_NUMBER = CallLog.Calls.NUMBER
        private const val COLUMN_PRESENTATION = CallLog.Calls.NUMBER_PRESENTATION
        private const val COLUMN_DATE = CallLog.Calls.DATE
        private const val COLUMN_DURATION = CallLog.Calls.DURATION
        private const val COLUMN_TYPE = CallLog.Calls.TYPE
        private const val COLUMN_CACHED_NAME = CallLog.Calls.CACHED_NAME

        private const val SORT_ORDER = "$COLUMN_DATE DESC"
        private val URI: Uri = CallLog.Calls.CONTENT_URI
        private val SELECTION: String? = null
        private val SELECTION_ARGS: Array<String>? = null
        private val PROJECTION = arrayOf(
                COLUMN_ID,
                COLUMN_NUMBER,
                COLUMN_DATE,
                COLUMN_DURATION,
                COLUMN_TYPE,
                COLUMN_CACHED_NAME,
                COLUMN_PRESENTATION)

        fun getRecentsCursor(context: Context): Cursor {
            return ContentResolverCompat.query(
                    context.contentResolver,
                    URI,
                    PROJECTION,
                    SELECTION,
                    SELECTION_ARGS,
                    SORT_ORDER,
                    CancellationSignal()
            )
        }

        fun getRecents(context: Context): Array<Recent> {
            val recents: MutableList<Recent> = ArrayList()
            val cursor = ContactsContentResolver.getContactsCursor(context)
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
    }

    override fun getContent(): Array<Recent> {
        return getRecents(context)
    }
}