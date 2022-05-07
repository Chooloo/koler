package com.chooloo.www.chooloolib.model

import com.chooloo.www.chooloolib.model.RawContactAccount.RawContactType
import com.chooloo.www.chooloolib.util.getRelativeDateString

data class ListData<DataType>(
    val items: List<DataType> = arrayListOf(),
    val headersToCounts: Map<String, Int> = emptyMap()
) {
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

        fun fromPhones(
            phones: List<PhoneAccount>,
            withHeader: Boolean = true
        ): ListData<PhoneAccount> {
            val phones = phones.toList().distinctBy { it.normalizedNumber }
            return ListData(phones, mapOf(Pair(if (withHeader) "Phones" else "", phones.size)))
        }

        fun fromRawContacts(
            rawContacts: List<RawContactAccount>,
            accounts: Boolean = false,
            withHeader: Boolean = true,
        ) = ListData(if (accounts) {
            rawContacts.filter {
                it.type in arrayOf(RawContactType.CUSTOM, RawContactType.WHATSAPP)
            }
        } else {
            rawContacts
        }, mapOf(
            Pair(
                if (withHeader) {
                    if (accounts) "Accounts" else "Raw Contacts"
                } else {
                    ""
                }, rawContacts.size
            )
        ))
    }
}