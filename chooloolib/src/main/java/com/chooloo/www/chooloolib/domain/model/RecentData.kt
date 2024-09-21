package com.chooloo.www.chooloolib.domain.model

import com.chooloo.www.chooloolib.domain.model.record.RecentRecord
import com.chooloo.www.chooloolib.utils.getRelativeDateString
import com.chooloo.www.chooloolib.utils.getTimeAgo
import java.util.Date

data class RecentData(
    var date: Date,
    var id: Long = 0,
    var number: String,
    var type: Int? = null,
    var duration: Long = 0,
    var name: String? = null,
    var typeLabel: String? = null,
    var groupAccounts: List<RecentData> = listOf()
) {
    val relativeTime by lazy { getTimeAgo(date.time) }

    companion object {
        val UNKNOWN = RecentData(date = Date(), number = "")

        fun fromRecord(record: RecentRecord) = RecentData(
            type = record.type,
            date = record.date,
            number = record.number,
            duration = record.duration,
            name = if (record.cachedName?.isNotEmpty() == true) record.cachedName else record.number
        )

        fun group(recents: List<RecentData>): List<RecentData> {
            if (recents.isEmpty()) {
                return emptyList()
            }

            var prevItem: RecentData = recents[0]
            val currentGroup = mutableListOf(prevItem)
            var prevDate = getRelativeDateString(prevItem.date)
            val groupedRecents = mutableListOf<RecentData>()

            recents.drop(1).forEach { curItem ->
                val curDate = getRelativeDateString(curItem.date)
                if (prevItem.number == curItem.number && prevDate == curDate) {
                    currentGroup.add(curItem)
                } else {
                    groupedRecents.add(prevItem.copy(groupAccounts = currentGroup.map { it.copy() }))
                    currentGroup.clear()
                    currentGroup.add(curItem)
                    prevItem = curItem
                }
                prevDate = curDate
            }
            groupedRecents.add(prevItem.copy(groupAccounts = currentGroup))
            return groupedRecents
        }
    }
}
