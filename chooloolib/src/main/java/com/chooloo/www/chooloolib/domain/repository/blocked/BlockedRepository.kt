package com.chooloo.www.chooloolib.domain.repository.blocked

import com.chooloo.www.chooloolib.domain.repository.base.BaseRepository

interface BlockedRepository : BaseRepository {
    suspend fun blockNumber(number: String)
    suspend fun unblockNumber(number: String)
    suspend fun isNumberBlocked(number: String): Boolean
}