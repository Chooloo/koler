package com.chooloo.www.chooloolib.domain.repository.recent

import androidx.compose.ui.graphics.vector.ImageVector
import com.chooloo.www.chooloolib.domain.model.record.RecentRecord
import com.chooloo.www.chooloolib.domain.repository.base.BaseRepository
import kotlinx.coroutines.flow.Flow

interface RecentRepository : BaseRepository {
    suspend fun getRecent(recentId: Long? = null): RecentRecord?
    fun getRecentFlow(recentId: Long? = null): Flow<RecentRecord?>
    suspend fun getRecents(filter: String? = null): List<RecentRecord>
    fun getRecentsFlow(filter: String? = null): Flow<List<RecentRecord>>

    suspend fun getLastOutgoingCall(): String
    suspend fun getCallTypeImage(@RecentRecord.CallType callType: Int): ImageVector

    suspend fun deleteRecent(recentId: Long)
}
