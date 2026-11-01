package com.chooloo.www.chooloolib.domain.model

import android.net.Uri
import android.provider.CallLog
import com.chooloo.www.chooloolib.domain.model.record.RecentRecord
import com.chooloo.www.chooloolib.utils.getRelativeDateString
import com.chooloo.www.chooloolib.utils.getTimeAgo
import java.util.Date

data class RecentData(
    val date: Date,
    val id: Long = 0,
    val number: String,
    val type: Int? = null,
    val duration: Long = 0,
    val name: String? = null,
    val photoUri: Uri? = null,
    val typeLabel: String? = null,
    val geocodedLocation: String? = null,
    val numberPresentation: Int = CallLog.Calls.PRESENTATION_ALLOWED,
    val groupAccounts: List<RecentData> = listOf()
) {
    val relativeTime by lazy { getTimeAgo(date.time) }

    companion object {
        val UNKNOWN = RecentData(date = Date(), number = "")

        fun fromRecord(record: RecentRecord, typeLabel: String? = null) = RecentData(
            id = record.id,
            type = record.type,
            date = record.date,
            number = record.number,
            typeLabel = typeLabel,
            duration = record.duration,
            groupAccounts = record.groupAccounts,
            geocodedLocation = record.geocodedLocation,
            numberPresentation = record.numberPresentation,
            photoUri = record.cachedPhotoUri?.let(Uri::parse),
            name = when {
                !record.cachedName.isNullOrEmpty() -> record.cachedName
                record.numberPresentation == CallLog.Calls.PRESENTATION_RESTRICTED -> "Restricted"
                record.numberPresentation == CallLog.Calls.PRESENTATION_PAYPHONE -> "Payphone"
                record.numberPresentation == CallLog.Calls.PRESENTATION_UNKNOWN || record.number.isEmpty() -> "Unknown"
                else -> record.number
            }
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
                    groupedRecents.add(prevItem.copy(groupAccounts = currentGroup.toList()))
                    currentGroup.clear()
                    currentGroup.add(curItem)
                    prevItem = curItem
                }
                prevDate = curDate
            }
            groupedRecents.add(prevItem.copy(groupAccounts = currentGroup.toList()))
            return groupedRecents
        }
    }
}
