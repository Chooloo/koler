package com.chooloo.www.chooloolib.testing

import com.chooloo.www.chooloolib.domain.model.Recent
import com.chooloo.www.chooloolib.domain.repository.RecentRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeRecentRepository(var lastOutgoingNumber: String? = null) : RecentRepository {
    var missedCallsReadMarks = 0

    override fun recents(filter: String): Flow<List<Recent>> = flowOf(emptyList())

    override fun recent(recentId: Long): Flow<Recent?> = flowOf(null)

    override fun recentsOfNumbers(numbers: List<String>): Flow<List<Recent>> = flowOf(emptyList())

    override suspend fun lastOutgoingNumber() = lastOutgoingNumber

    override suspend fun deleteAllRecents() = Unit

    override suspend fun markMissedCallsRead() {
        missedCallsReadMarks++
    }
}
