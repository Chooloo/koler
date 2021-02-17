package com.chooloo.www.koler.util

import android.content.Context
import android.provider.CallLog
import com.chooloo.www.koler.contentresolver.RecentsContentResolver
import com.chooloo.www.koler.entity.Recent

fun Context.getRecentById(recentId: Long): Recent {
    RecentsContentResolver(this, recentId).content.also {
        return if (it.isNotEmpty()) it[0] else Recent.UNKNOWN
    }
}

fun Context.deleteRecent(recentId: Long) {
    contentResolver.delete(CallLog.Calls.CONTENT_URI, "${CallLog.Calls._ID} = $recentId", null)
}