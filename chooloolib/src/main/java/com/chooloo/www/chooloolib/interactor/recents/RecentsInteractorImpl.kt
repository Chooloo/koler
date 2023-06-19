package com.chooloo.www.chooloolib.interactor.recents

import android.Manifest.permission.WRITE_CALL_LOG
import android.content.Context
import android.provider.CallLog
import androidx.annotation.RequiresPermission
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.data.model.RecentAccount
import com.chooloo.www.chooloolib.data.repository.recents.RecentsRepository
import com.chooloo.www.chooloolib.interactor.base.BaseInteractorImpl
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecentsInteractorImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val recentsRepository: RecentsRepository
) : BaseInteractorImpl<RecentsInteractor.Listener>(), RecentsInteractor {

    @RequiresPermission(WRITE_CALL_LOG)
    override fun deleteRecent(recentId: Long) {
        context.contentResolver.delete(
            CallLog.Calls.CONTENT_URI,
            String.format("%s = %s", CallLog.Calls._ID, recentId),
            null
        )
    }

    @RequiresPermission(WRITE_CALL_LOG)
    override fun deleteAllRecents() {
        context.contentResolver.delete(
            CallLog.Calls.CONTENT_URI,
            null,
            null
        )
    }

    override fun getRecent(recentId: Long) = recentsRepository.getRecent(recentId)

    override fun getRecents() = recentsRepository.getRecents()

    override fun getCallTypeImage(@RecentAccount.CallType callType: Int) = when (callType) {
        RecentAccount.TYPE_INCOMING -> R.drawable.call_received
        RecentAccount.TYPE_OUTGOING -> R.drawable.call_made
        RecentAccount.TYPE_MISSED -> R.drawable.call_missed
        RecentAccount.TYPE_REJECTED -> R.drawable.call_missed_outgoing
        RecentAccount.TYPE_VOICEMAIL -> R.drawable.voicemail
        RecentAccount.TYPE_BLOCKED -> R.drawable.block
        else -> R.drawable.call_made
    }

    override fun getLastOutgoingCall(): String = CallLog.Calls.getLastOutgoingCall(context)
}