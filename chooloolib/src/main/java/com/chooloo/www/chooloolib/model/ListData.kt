package com.chooloo.www.chooloolib.model

import androidx.annotation.StringRes
import com.chooloo.www.chooloolib.BaseApp
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.util.getRelativeDateString

data class ListData<DataType>(
    val items: List<DataType> = arrayListOf(),
    val headersToCounts: Map<String, Int> = emptyMap()
) {
    object Strings {
        fun get(@StringRes stringRes: Int, vararg formatArgs: Any = emptyArray()): String {
            return BaseApp.instance.getString(stringRes, *formatArgs)
        }
    }
    companion object {
        fun fromContacts(
            contacts: List<ContactAccount>,
            withFavs: Boolean = false
        ): ListData<ContactAccount> {
            val headersToCounts = contacts.groupingBy { it.name?.get(0).toString() }.eachCount()
            if (withFavs) {
                val favs = contacts.filter { it.starred }
                if (favs.isNotEmpty()) {
                    return ListData(
                        items = ArrayList(favs + contacts),
                        headersToCounts = mapOf(Pair("★", favs.size)) + headersToCounts
                    )
                }
            }
            return ListData(contacts, headersToCounts)
        }

        fun fromRecents(recents: List<RecentAccount>, isGrouped: Boolean) =
            if (isGrouped && recents.size > 1) {
                getGroupedRecents(recents)
            } else {
                ListData(
                    recents,
                    recents.groupingBy { getRelativeDateString(it.date) }.eachCount()
                )
            }

        private fun getGroupedRecents(recents: List<RecentAccount>): ListData<RecentAccount> {
            var prevItem: RecentAccount = recents[0]
            val currentGroup = mutableListOf(prevItem)
            var prevDate = getRelativeDateString(prevItem.date)
            val groupedRecents = mutableListOf<RecentAccount>()

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

            return ListData(
                groupedRecents,
                groupedRecents.groupingBy { getRelativeDateString(it.date) }.eachCount()
            )
        }

        fun fromPhones(phones: List<PhoneAccount>): ListData<PhoneAccount> {
            val phones = phones.toList().distinctBy { it.normalizedNumber }
            return ListData(phones, mapOf(Pair(Strings.get(R.string.hint_phones), phones.size)))
        }
    }
}