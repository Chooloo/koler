package com.chooloo.www.koler.contentresolver

import android.Manifest.permission.READ_CALL_LOG
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.CallLog
import com.chooloo.www.koler.R
import com.chooloo.www.koler.data.Recent
import com.chooloo.www.koler.data.RecentsBundle
import java.util.*

class RecentsContentResolver(
    context: Context,
    private val recentId: Long? = null
) : BaseContentResolver<RecentsBundle>(context) {

    companion object {
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

    override val requiredPermissions: Array<String> = arrayOf(READ_CALL_LOG)

    override val uri: Uri = CallLog.Calls.CONTENT_URI

    override val filterUri: Uri? = null

    override val sortOrder = "${CallLog.Calls.DATE} DESC"

    override val selection: String
        get() {
            val selection = SelectionBuilder().addSelection(CallLog.Calls._ID, recentId)
            filter?.let { selection.addString("(${CallLog.Calls.CACHED_NAME} LIKE '%$filter%' OR ${CallLog.Calls.NUMBER} LIKE '%$filter%')") }
            return selection.build()
        }

    override val projection = arrayOf(
        CallLog.Calls._ID,
        CallLog.Calls.NUMBER,
        CallLog.Calls.NUMBER_PRESENTATION,
        CallLog.Calls.DATE,
        CallLog.Calls.DURATION,
        CallLog.Calls.CACHED_NAME,
        CallLog.Calls.TYPE
    )

    override val selectionArgs: Array<String>? = null

    override fun convertCursorToContent(cursor: Cursor?) = RecentsBundle(
        recents = ArrayList<Recent>().apply {
            while (cursor != null && cursor.moveToNext()) cursor.apply {
                add(
                    Recent(
                        id = getLong(getColumnIndex(CallLog.Calls._ID)),
                        number = getString(getColumnIndex(CallLog.Calls.NUMBER)),
                        duration = getLong(getColumnIndex(CallLog.Calls.DURATION)),
                        date = Date(getLong(getColumnIndex(CallLog.Calls.DATE))),
                        type = getInt(getColumnIndex(CallLog.Calls.TYPE)),
                        cachedName = getString(getColumnIndex(CallLog.Calls.CACHED_NAME))
                    )
                )
            }
        }
    ).also {
        cursor?.close()
    }

}