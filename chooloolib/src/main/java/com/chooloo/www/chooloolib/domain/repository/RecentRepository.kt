package com.chooloo.www.chooloolib.domain.repository

import com.chooloo.www.chooloolib.domain.model.Recent
import kotlinx.coroutines.flow.Flow

interface RecentRepository {
    fun recents(filter: String? = null): Flow<List<Recent>>
    fun recent(recentId: Long): Flow<Recent?>
    fun recentsOfNumbers(numbers: List<String>): Flow<List<Recent>>
    suspend fun lastOutgoingNumber(): String?
    suspend fun deleteRecent(recentId: Long)
    suspend fun deleteAllRecents()
}
