package com.chooloo.www.chooloolib.domain.repository.blocked

import com.chooloo.www.chooloolib.domain.model.record.BlockedNumberRecord
import kotlinx.coroutines.flow.Flow

interface BlockedRepository {
    suspend fun blockNumber(number: String)
    suspend fun unblockNumber(number: String)
    suspend fun isNumberBlocked(number: String): Boolean
    suspend fun getBlockedNumbers(filter: String? = null): List<BlockedNumberRecord>
    fun getBlockedNumbersFlow(filter: String? = null): Flow<List<BlockedNumberRecord>>
}
