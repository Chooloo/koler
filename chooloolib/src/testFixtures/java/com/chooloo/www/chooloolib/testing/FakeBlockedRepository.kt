package com.chooloo.www.chooloolib.testing

import com.chooloo.www.chooloolib.domain.repository.BlockedRepository

class FakeBlockedRepository(initial: Set<String> = emptySet()) : BlockedRepository {
    private val numbers = initial.toMutableSet()
    val blocked: Set<String> get() = numbers
    val checkedNumbers = mutableListOf<String>()

    override suspend fun isBlocked(number: String): Boolean {
        checkedNumbers += number
        return number in numbers
    }

    override suspend fun block(number: String) {
        numbers += number
    }

    override suspend fun unblock(number: String) {
        numbers -= number
    }
}
