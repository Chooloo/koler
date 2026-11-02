package com.chooloo.www.chooloolib.domain.model

import java.time.LocalDateTime
import java.time.ZoneOffset
import org.junit.Assert.assertEquals
import org.junit.Test

class RecentGroupTest {
    private fun recent(id: Long, number: String, dayOfMonth: Int, hour: Int = 12) = Recent(
        id = id,
        number = number,
        dateMillis = LocalDateTime.of(2026, 1, dayOfMonth, hour, 0).toInstant(ZoneOffset.UTC).toEpochMilli(),
        type = Recent.Type.INCOMING,
    )

    @Test
    fun `consecutive calls from the same number on the same day are grouped`() {
        val recents = listOf(recent(3, "111", 2, 18), recent(2, "111", 2, 9), recent(1, "222", 2, 8))

        val groups = recents.groupedByNumberAndDay(ZoneOffset.UTC)

        assertEquals(listOf(listOf(3L, 2L), listOf(1L)), groups.map { group -> group.recents.map { it.id } })
        assertEquals(3L, groups.first().latest.id)
    }

    @Test
    fun `same number on different days is not grouped`() {
        val groups = listOf(recent(2, "111", 3), recent(1, "111", 2)).groupedByNumberAndDay(ZoneOffset.UTC)

        assertEquals(2, groups.size)
    }

    @Test
    fun `same number separated by another call is not grouped`() {
        val recents = listOf(recent(3, "111", 2), recent(2, "222", 2), recent(1, "111", 2))

        assertEquals(3, recents.groupedByNumberAndDay(ZoneOffset.UTC).size)
    }

    @Test
    fun `empty list gives no groups`() {
        assertEquals(emptyList<RecentGroup>(), emptyList<Recent>().groupedByNumberAndDay(ZoneOffset.UTC))
    }

    @Test
    fun `ungrouped wraps each recent in its own group`() {
        val recents = listOf(recent(2, "111", 2), recent(1, "111", 2))

        assertEquals(listOf(1, 1), recents.ungrouped().map { it.recents.size })
    }
}
