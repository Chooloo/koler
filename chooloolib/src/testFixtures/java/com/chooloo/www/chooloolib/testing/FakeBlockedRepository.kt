package com.chooloo.www.chooloolib.testing

import com.chooloo.www.chooloolib.domain.repository.BlockedRepository

class FakeBlockedRepository(private val blocked: Set<String> = emptySet()) : BlockedRepository {
    val checkedNumbers = mutableListOf<String>()

    override suspend fun isBlocked(number: String): Boolean {
        checkedNumbers += number
        return number in blocked
    }

    override suspend fun block(number: String) = Unit

    override suspend fun unblock(number: String) = Unit
}
