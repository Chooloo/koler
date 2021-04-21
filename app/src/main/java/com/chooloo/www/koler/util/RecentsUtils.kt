package com.chooloo.www.koler.util

import android.content.Context
import android.provider.CallLog
import com.chooloo.www.koler.contentresolver.RecentsContentResolver
import com.chooloo.www.koler.data.Recent

fun Context.deleteRecent(recentId: Long) {
    contentResolver.delete(CallLog.Calls.CONTENT_URI, "${CallLog.Calls._ID} = $recentId", null)
}

fun Context.getRecentById(recentId: Long) =
    RecentsContentResolver(this, recentId).content.recents.getOrNull(0) ?: Recent.UNKNOWN
