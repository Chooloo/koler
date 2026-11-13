package com.chooloo.www.chooloolib.domain.model

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

data class RecentGroup(val recents: List<Recent>) {
    val latest: Recent get() = recents.first()

    companion object {
        fun ungrouped(recents: List<Recent>): List<RecentGroup> =
            recents.map { RecentGroup(listOf(it)) }

        fun groupedByNumberAndDay(
            recents: List<Recent>,
            zone: ZoneId = ZoneId.systemDefault(),
        ): List<RecentGroup> {
            val groups = mutableListOf<MutableList<Recent>>()
            for (recent in recents) {
                val current = groups.lastOrNull()
                if (current != null && current.last().number == recent.number &&
                    dayOf(current.last(), zone) == dayOf(recent, zone)
                ) {
                    current.add(recent)
                } else {
                    groups.add(mutableListOf(recent))
                }
            }
            return groups.map(::RecentGroup)
        }

        private fun dayOf(recent: Recent, zone: ZoneId): LocalDate =
            Instant.ofEpochMilli(recent.dateMillis).atZone(zone).toLocalDate()
    }
}
