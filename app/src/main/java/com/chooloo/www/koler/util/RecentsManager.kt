package com.chooloo.www.koler.util

import android.provider.CallLog
import com.chooloo.www.koler.R
import com.chooloo.www.koler.contentresolver.RecentsContentResolver
import com.chooloo.www.koler.data.Recent
import com.chooloo.www.koler.ui.base.BaseActivity
import com.chooloo.www.koler.util.permissions.PermissionsManager

class RecentsManager(private val _activity: BaseActivity) {
    private val _permissionsManager by lazy { PermissionsManager(_activity) }


    fun getRecentById(recentId: Long) =
        RecentsContentResolver(_activity, recentId).content.getOrNull(0) ?: Recent.UNKNOWN

    fun deleteRecent(recentId: Long) {
        _permissionsManager.runWithPrompt(R.string.warning_delete_recent) {
            _activity.contentResolver.delete(
                CallLog.Calls.CONTENT_URI,
                "${CallLog.Calls._ID} = $recentId",
                null
            )
        }
    }
}