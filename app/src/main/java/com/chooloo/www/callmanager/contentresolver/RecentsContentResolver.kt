package com.chooloo.www.callmanager.contentresolver

import android.Manifest.permission.READ_CALL_LOG
import android.content.Context
import android.provider.CallLog
import com.chooloo.www.callmanager.entity.Recent
import java.util.*

class RecentsContentResolver(context: Context) : BaseContentResolver<Array<Recent>>(
        context = context,
        defaultUri = CallLog.Calls.CONTENT_URI_WITH_VOICEMAIL,
        filterUri = CallLog.Calls.CONTENT_FILTER_URI,
        sortOrder = "$COLUMN_DATE DESC",
        projection = arrayOf(COLUMN_ID, COLUMN_NUMBER, COLUMN_DATE, COLUMN_DURATION, COLUMN_TYPE, COLUMN_CACHED_NAME, COLUMN_PRESENTATION)
) {

    companion object {
        const val REQUIRED_PERMISSION = READ_CALL_LOG

        private const val COLUMN_ID = CallLog.Calls._ID
        private const val COLUMN_NUMBER = CallLog.Calls.NUMBER
        private const val COLUMN_PRESENTATION = CallLog.Calls.NUMBER_PRESENTATION
        private const val COLUMN_DATE = CallLog.Calls.DATE
        private const val COLUMN_DURATION = CallLog.Calls.DURATION
        private const val COLUMN_TYPE = CallLog.Calls.TYPE
        private const val COLUMN_CACHED_NAME = CallLog.Calls.CACHED_NAME
    }

    override fun getContent(): Array<Recent> {
        return ArrayList<Recent>().apply {
            val cursor = queryContent()
            while (cursor != null && cursor.moveToNext()) {
                add(Recent(
                        callId = cursor.getLong(cursor.getColumnIndex(COLUMN_ID)),
                        callerNumber = cursor.getString(cursor.getColumnIndex(COLUMN_NUMBER)),
                        callDuration = cursor.getString(cursor.getColumnIndex(COLUMN_DURATION)),
                        callDate = Date(cursor.getLong(cursor.getColumnIndex(COLUMN_DATE))),
                        callType = cursor.getInt(cursor.getColumnIndex(COLUMN_TYPE))
                ))
            }
            cursor?.close()
        }.toTypedArray()
    }
}