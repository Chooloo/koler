package com.chooloo.www.koler.contentresolver

import android.Manifest.permission.READ_CALL_LOG
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.CallLog
import com.chooloo.www.koler.R
import com.chooloo.www.koler.data.account.Recent
import com.chooloo.www.koler.util.SelectionBuilder
import java.util.*

class RecentsContentResolver(context: Context, private val recentId: Long? = null) :
    BaseItemsContentResolver<Recent>(context) {

    override val uri: Uri = URI
    override val filterUri: Uri = FILTER_URI
    override val sortOrder: String = SORT_ORDER
    override val selectionArgs: Array<String>? = null
    override val projection: Array<String> = PROJECTION
    override val selection
        get() = SelectionBuilder().addSelection(CallLog.Calls._ID, recentId).build()


    override fun convertCursorToItem(cursor: Cursor): Recent {
        return Recent(
            id = cursor.getLong(cursor.getColumnIndex(CallLog.Calls._ID)),
            number = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER)),
            duration = cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DURATION)),
            date = Date(cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DATE))),
            type = cursor.getInt(cursor.getColumnIndex(CallLog.Calls.TYPE)),
            cachedName = cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME))
        )
    }

    companion object {
        val URI: Uri = CallLog.Calls.CONTENT_URI
        val SORT_ORDER = "${CallLog.Calls.DATE} DESC"
        val REQUIRED_PERMISSIONS = arrayOf(READ_CALL_LOG)
        val FILTER_URI: Uri = CallLog.Calls.CONTENT_FILTER_URI
        val PROJECTION: Array<String> = arrayOf(
            CallLog.Calls._ID,
            CallLog.Calls.NUMBER,
            CallLog.Calls.NUMBER_PRESENTATION,
            CallLog.Calls.DATE,
            CallLog.Calls.DURATION,
            CallLog.Calls.CACHED_NAME,
            CallLog.Calls.TYPE
        )

        fun getCallTypeImage(@Recent.CallType callType: Int) = when (callType) {
            Recent.TYPE_INCOMING -> R.drawable.ic_call_received_black_24dp
            Recent.TYPE_OUTGOING -> R.drawable.ic_call_made_black_24dp
            Recent.TYPE_MISSED -> R.drawable.ic_call_missed_black_24dp
            Recent.TYPE_REJECTED -> R.drawable.ic_call_missed_outgoing_black_24dp
            Recent.TYPE_VOICEMAIL -> R.drawable.ic_voicemail_black_24dp
            Recent.TYPE_BLOCKED -> R.drawable.round_block_24
            else -> R.drawable.ic_call_made_black_24dp
        }
    }
}