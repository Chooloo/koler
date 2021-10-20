package com.chooloo.www.koler.data

import android.provider.ContactsContract.CommonDataKinds.Phone
import com.chooloo.www.koler.data.account.Contact
import com.chooloo.www.koler.data.account.PhoneAccount
import com.chooloo.www.koler.data.account.Recent
import com.chooloo.www.koler.interactor.string.StringInteractor
import com.chooloo.www.koler.util.getRelativeDateString
import java.util.stream.Collectors

data class ListBundle<DataType>(
    val items: ArrayList<DataType> = arrayListOf(),
    val headers: Array<String> = arrayOf(),
    val headersCounts: Array<Int> = arrayOf()
) {
    companion object {
        fun fromContacts(
            contacts: ArrayList<Contact>,
            withFavs: Boolean = false
        ): ListBundle<Contact> {
            val headers =
                contacts.stream().map { contact -> contact.name?.get(0).toString() }.distinct()
                    .collect(Collectors.toList()).toTypedArray()
            val headersCounts = arrayListOf<Int>()
            headers.forEach { header ->
                headersCounts.add(contacts.count { contact ->
                    contact.name?.get(0).toString() == header
                })
            }
            if (withFavs) {
                val favs = ArrayList(contacts.filter { it.starred })
                if (favs.isNotEmpty()) {
                    return ListBundle(
                        items = ArrayList(favs + contacts),
                        headers = arrayOf("★") + headers,
                        headersCounts = arrayOf(favs.size) + headersCounts
                    )
                }
            }
            return ListBundle(
                items = contacts,
                headers = headers,
                headersCounts = headersCounts.toTypedArray()
            )
        }

        fun fromRecents(recents: ArrayList<Recent>): ListBundle<Recent> {
            if (recents.isEmpty()) {
                return ListBundle()
            }
            val headers: ArrayList<String> = arrayListOf()
            val headersCounts: ArrayList<Int> = arrayListOf()
            var currentCount = 0
            var currentHeader = getRelativeDateString(recents[0].date)
            recents.forEach {
                val dateString = getRelativeDateString(it.date)
                if (dateString != currentHeader) {
                    headers.add(currentHeader)
                    headersCounts.add(currentCount)
                    currentHeader = dateString
                    currentCount = 1
                } else {
                    currentCount = currentCount.plus(1)
                }
            }
            return ListBundle(
                items = recents,
                headers = headers.toTypedArray(),
                headersCounts = headersCounts.toTypedArray()
            )
        }

        fun fromPhones(
            phones: ArrayList<PhoneAccount>,
            stringInteractor: StringInteractor,
            distinctNormalizedNumber: Boolean = false
        ) = ListBundle(
            items = if (distinctNormalizedNumber) {
                ArrayList(phones.toList().distinctBy { it.normalizedNumber })
            } else {
                phones
            }
        )

        fun fromPhones1(
            phones: ArrayList<PhoneAccount>,
            stringInteractor: StringInteractor,
            distinctNormalizedNumber: Boolean = false
        ): ListBundle<PhoneAccount> {
            val items = if (distinctNormalizedNumber) {
                ArrayList(phones.toList().distinctBy { it.normalizedNumber })
            } else {
                phones
            }
            return ListBundle(
                items = items,
                headersCounts = items.groupingBy { it.type }.eachCount().values.toTypedArray(),
                headers = items.groupBy { it.type }.keys.map {
                    stringInteractor.getString(Phone.getTypeLabelResource(it))
                }.toTypedArray()
            )
        }
    }
}