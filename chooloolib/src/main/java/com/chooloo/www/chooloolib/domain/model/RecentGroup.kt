package com.chooloo.www.chooloolib.domain.model

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

data class RecentGroup(val recents: List<Recent>) {
    val latest: Recent get() = recents.first()
}

fun List<Recent>.ungrouped(): List<RecentGroup> = map { RecentGroup(listOf(it)) }

fun List<Recent>.groupedByNumberAndDay(zone: ZoneId = ZoneId.systemDefault()): List<RecentGroup> {
    fun Recent.day(): LocalDate = Instant.ofEpochMilli(dateMillis).atZone(zone).toLocalDate()

    val groups = mutableListOf<MutableList<Recent>>()
    for (recent in this) {
        val current = groups.lastOrNull()
        if (current != null && current.last().number == recent.number && current.last().day() == recent.day()) {
            current.add(recent)
        } else {
            groups.add(mutableListOf(recent))
        }
    }
    return groups.map(::RecentGroup)
}
