package com.chooloo.www.koler.interactor.recents

import android.Manifest.permission.WRITE_CALL_LOG
import android.content.Context
import android.provider.CallLog
import androidx.annotation.RequiresPermission
import com.chooloo.www.koler.contentresolver.RecentsContentResolver
import com.chooloo.www.koler.data.account.Recent
import com.chooloo.www.koler.interactor.base.BaseInteractorImpl

class RecentsInteractorImpl(
    private val context: Context
) : BaseInteractorImpl<RecentsInteractor.Listener>(), RecentsInteractor {
    override fun queryRecent(recentId: Long) =
        RecentsContentResolver(context, recentId).queryContent().getOrNull(0)

    override fun queryRecent(recentId: Long, callback: (Recent?) -> Unit) {
        RecentsContentResolver(context, recentId).queryContent {
            callback.invoke(it?.getOrNull(0))
        }
    }

    @RequiresPermission(WRITE_CALL_LOG)
    override fun deleteRecent(recentId: Long) {
        context.contentResolver.delete(
            CallLog.Calls.CONTENT_URI,
            String.format("%s = %s", CallLog.Calls._ID, recentId),
            null
        )
    }
}