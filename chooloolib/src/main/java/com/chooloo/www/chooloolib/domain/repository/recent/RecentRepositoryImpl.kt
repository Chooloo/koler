package com.chooloo.www.chooloolib.domain.repository.recent

import android.Manifest.permission.WRITE_CALL_LOG
import android.content.Context
import android.provider.CallLog
import androidx.annotation.RequiresPermission
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.CallMade
import androidx.compose.material.icons.automirrored.rounded.CallMissed
import androidx.compose.material.icons.automirrored.rounded.CallMissedOutgoing
import androidx.compose.material.icons.automirrored.rounded.CallReceived
import androidx.compose.material.icons.rounded.Block
import androidx.compose.material.icons.rounded.Voicemail
import com.chooloo.www.chooloolib.di.factory.contentresolver.ContentResolverFactory
import com.chooloo.www.chooloolib.domain.model.record.RecentRecord
import com.chooloo.www.chooloolib.domain.repository.base.BaseRepositoryImpl
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecentRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val contentResolverFactory: ContentResolverFactory
) : BaseRepositoryImpl(), RecentRepository {
    override fun getRecentFlow(recentId: Long?) = flow {
        contentResolverFactory.getRecentsContentResolver(recentId = recentId).getItemsFlow()
            .collect { emit(it.getOrNull(0)) }
    }

    override suspend fun getRecent(recentId: Long?): RecentRecord? =
        contentResolverFactory.getRecentsContentResolver(recentId = recentId).getItems()
            .getOrNull(0)

    override suspend fun getRecents(filter: String?): List<RecentRecord> =
        contentResolverFactory.getRecentsContentResolver(filter = filter).getItems()

    override fun getRecentsFlow(filter: String?): Flow<List<RecentRecord>> =
        contentResolverFactory.getRecentsContentResolver(filter = filter).getItemsFlow()

    override suspend fun getCallTypeImage(@RecentRecord.CallType callType: Int) =
        when (callType) {
            RecentRecord.TYPE_BLOCKED -> Icons.Rounded.Block
            RecentRecord.TYPE_VOICEMAIL -> Icons.Rounded.Voicemail
            RecentRecord.TYPE_OUTGOING -> Icons.AutoMirrored.Rounded.CallMade
            RecentRecord.TYPE_MISSED -> Icons.AutoMirrored.Rounded.CallMissed
            RecentRecord.TYPE_INCOMING -> Icons.AutoMirrored.Rounded.CallReceived
            RecentRecord.TYPE_REJECTED -> Icons.AutoMirrored.Rounded.CallMissedOutgoing
            else -> Icons.AutoMirrored.Rounded.CallMade
        }

    override suspend fun getLastOutgoingCall(): String =
        CallLog.Calls.getLastOutgoingCall(context)


    @RequiresPermission(WRITE_CALL_LOG)
    override suspend fun deleteRecent(recentId: Long) {
        context.contentResolver.delete(
            CallLog.Calls.CONTENT_URI, String.format("%s = %s", CallLog.Calls._ID, recentId), null
        )
    }
}