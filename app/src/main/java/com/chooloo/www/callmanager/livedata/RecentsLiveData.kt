package com.chooloo.www.callmanager.livedata

import android.content.Context
import android.net.Uri
import android.provider.CallLog

class RecentsLiveData(context: Context) : CursorLiveData(context) {
    companion object {
        private const val COLUMN_ID = CallLog.Calls._ID
        private const val COLUMN_NUMBER = CallLog.Calls.NUMBER
        private const val COLUMN_PRESENTATION = CallLog.Calls.NUMBER_PRESENTATION
        private const val COLUMN_DATE = CallLog.Calls.DATE
        private const val COLUMN_DURATION = CallLog.Calls.DURATION
        private const val COLUMN_TYPE = CallLog.Calls.TYPE
        private const val COLUMN_CACHED_NAME = CallLog.Calls.CACHED_NAME

        private const val RECENTS_ORDER = "$COLUMN_DATE DESC"
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

    override fun onGetUri(): Uri = RECENTS_URI
    override fun onGetProjection(): Array<String>? = RECENTS_PROJECTION
    override fun onGetSelection(): String? = RECENTS_SELECTION
    override fun onGetSelectionArgs(): Array<String>? = RECENTS_SELECTION_ARGS
    override fun onGetSortOrder(): String? = RECENTS_ORDER
}