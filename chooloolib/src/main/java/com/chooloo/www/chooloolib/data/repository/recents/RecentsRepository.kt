package com.chooloo.www.chooloolib.data.repository.recents

import com.chooloo.www.chooloolib.data.model.RecentAccount
import kotlinx.coroutines.flow.Flow

interface RecentsRepository {
    fun getRecent(recentId: Long? = null): Flow<RecentAccount?>
    fun getRecents(): Flow<List<RecentAccount>>
}