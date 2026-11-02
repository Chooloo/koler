package com.chooloo.www.chooloolib.domain.repository

interface BlockedRepository {
    suspend fun isBlocked(number: String): Boolean
    suspend fun block(number: String)
    suspend fun unblock(number: String)
}
