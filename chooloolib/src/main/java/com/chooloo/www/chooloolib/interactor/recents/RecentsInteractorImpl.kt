package com.chooloo.www.chooloolib.interactor.recents

import android.Manifest.permission.WRITE_CALL_LOG
import android.content.Context
import android.provider.CallLog
import androidx.annotation.RequiresPermission
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.contentresolver.RecentsContentResolver
import com.chooloo.www.chooloolib.interactor.base.BaseInteractorImpl
import com.chooloo.www.chooloolib.model.RecentAccount
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecentsInteractorImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : BaseInteractorImpl<RecentsInteractor.Listener>(), RecentsInteractor {

    @RequiresPermission(WRITE_CALL_LOG)
    override fun deleteRecent(recentId: Long) {
        context.contentResolver.delete(
            CallLog.Calls.CONTENT_URI,
            String.format("%s = %s", CallLog.Calls._ID, recentId),
            null
        )
    }

    override fun queryRecent(recentId: Long) =
        RecentsContentResolver(context, recentId).queryItems().getOrNull(0)

    override fun queryRecent(recentId: Long, callback: (RecentAccount?) -> Unit) {
        RecentsContentResolver(context, recentId).queryItems {
            callback.invoke(it.getOrNull(0))
        }
    }

    override fun getCallTypeImage(@RecentAccount.CallType callType: Int) = when (callType) {
        RecentAccount.TYPE_INCOMING -> R.drawable.round_call_received_20
        RecentAccount.TYPE_OUTGOING -> R.drawable.round_call_made_20
        RecentAccount.TYPE_MISSED -> R.drawable.round_call_missed_20
        RecentAccount.TYPE_REJECTED -> R.drawable.round_call_missed_outgoing_20
        RecentAccount.TYPE_VOICEMAIL -> R.drawable.round_voicemail_20
        RecentAccount.TYPE_BLOCKED -> R.drawable.round_block_24
        else -> R.drawable.round_call_made_20
    }

    override fun getLastOutgoingCall(): String = CallLog.Calls.getLastOutgoingCall(context)
}