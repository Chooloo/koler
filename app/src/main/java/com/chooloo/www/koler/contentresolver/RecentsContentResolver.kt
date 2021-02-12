package com.chooloo.www.koler.contentresolver

import android.Manifest.permission.READ_CALL_LOG
import android.Manifest.permission.READ_VOICEMAIL
import android.content.Context
import android.database.Cursor
import android.provider.CallLog
import com.chooloo.www.koler.entity.Recent
import java.util.*

class RecentsContentResolver(context: Context) : BaseContentResolver<Array<Recent>>(context) {

    override val requiredPermissions: Array<String>
        get() = arrayOf(READ_CALL_LOG, READ_VOICEMAIL)

    override fun onGetDefaultUri() = CallLog.Calls.CONTENT_URI
    override fun onGetFilterUri() = CallLog.Calls.CONTENT_FILTER_URI
    override fun onGetSelection() = null
    override fun onGetSortOrder() = "${CallLog.Calls.DATE} DESC"
    override fun onGetSelectionArgs() = null
    override fun onGetProjection() = arrayOf(
            CallLog.Calls._ID,
            CallLog.Calls.NUMBER,
            CallLog.Calls.NUMBER_PRESENTATION,
            CallLog.Calls.DATE,
            CallLog.Calls.DURATION,
            CallLog.Calls.CACHED_NAME,
            CallLog.Calls.TYPE
    )

    override fun convertCursorToContent(cursor: Cursor?) = ArrayList<Recent>().apply {
        while (cursor != null && cursor.moveToNext()) cursor.apply {
            add(Recent(
                    callId = getLong(getColumnIndex(CallLog.Calls._ID)),
                    callerNumber = getString(getColumnIndex(CallLog.Calls.NUMBER)),
                    callDuration = getString(getColumnIndex(CallLog.Calls.DURATION)),
                    callDate = Date(getLong(getColumnIndex(CallLog.Calls.DATE))),
                    callType = getInt(getColumnIndex(CallLog.Calls.TYPE)),
                    cachedName = getString(getColumnIndex(CallLog.Calls.CACHED_NAME))
            ))
        }
        cursor?.close()
    }.toTypedArray()
}