package com.chooloo.www.chooloolib.data.contentresolver

import android.content.ContentResolver
import android.database.Cursor
import android.net.Uri
import android.provider.CallLog
import com.chooloo.www.chooloolib.domain.model.record.RecentRecord
import com.chooloo.www.chooloolib.utils.SelectionBuilder
import com.chooloo.www.chooloolib.utils.getInt
import com.chooloo.www.chooloolib.utils.getLong
import com.chooloo.www.chooloolib.utils.getString
import java.util.Date

class RecentsContentResolver(
    filter: String? = null,
    contentResolver: ContentResolver,
    recentId: Long? = null,
) : BaseContentResolver<RecentRecord>(filter, contentResolver) {

    private val selectionBuilder = SelectionBuilder().apply {
        addSelection(CallLog.Calls._ID, recentId)
        currentFilter?.let {
            val pattern = "%$it%"
            addRaw(
                "(${CallLog.Calls.CACHED_NAME} LIKE ? OR ${CallLog.Calls.NUMBER} LIKE ?)",
                pattern,
                pattern
            )
        }
    }

    override val uri: Uri = CallLog.Calls.CONTENT_URI
    override val filterUri: Uri? = null
    override val sortOrder: String = "${CallLog.Calls.DATE} DESC"
    override val selection: String? = selectionBuilder.build()
    override val selectionArgs: Array<String>? = selectionBuilder.buildArgs()
    override val projection: Array<String> = arrayOf(
        CallLog.Calls._ID,
        CallLog.Calls.NUMBER,
        CallLog.Calls.NUMBER_PRESENTATION,
        CallLog.Calls.DATE,
        CallLog.Calls.DURATION,
        CallLog.Calls.CACHED_NAME,
        CallLog.Calls.CACHED_PHOTO_URI,
        CallLog.Calls.CACHED_NUMBER_TYPE,
        CallLog.Calls.CACHED_NUMBER_LABEL,
        CallLog.Calls.GEOCODED_LOCATION,
        CallLog.Calls.TYPE
    )

    override fun convertCursorToItem(cursor: Cursor) = RecentRecord(
        id = cursor.getLong(CallLog.Calls._ID),
        type = cursor.getInt(CallLog.Calls.TYPE),
        date = Date(cursor.getLong(CallLog.Calls.DATE)),
        number = cursor.getString(CallLog.Calls.NUMBER) ?: "",
        duration = cursor.getLong(CallLog.Calls.DURATION),
        cachedName = cursor.getString(CallLog.Calls.CACHED_NAME),
        cachedPhotoUri = cursor.getString(CallLog.Calls.CACHED_PHOTO_URI),
        cachedNumberType = cursor.getInt(CallLog.Calls.CACHED_NUMBER_TYPE),
        cachedNumberLabel = cursor.getString(CallLog.Calls.CACHED_NUMBER_LABEL),
        geocodedLocation = cursor.getString(CallLog.Calls.GEOCODED_LOCATION),
        numberPresentation = cursor.getInt(CallLog.Calls.NUMBER_PRESENTATION)
    )
}
