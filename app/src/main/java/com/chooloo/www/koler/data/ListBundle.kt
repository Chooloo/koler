package com.chooloo.www.koler.data

import android.provider.ContactsContract.CommonDataKinds.Phone
import com.chooloo.www.koler.data.account.Contact
import com.chooloo.www.koler.data.account.PhoneAccount
import com.chooloo.www.koler.data.account.Recent
import com.chooloo.www.koler.interactor.string.StringInteractor
import com.chooloo.www.koler.util.getRelativeDateString

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
            val headers = contacts.groupingBy { it.name?.get(0).toString() }.eachCount()
            if (withFavs) {
                val favs = ArrayList(contacts.filter { it.starred })
                if (favs.isNotEmpty()) {
                    return ListBundle(
                        items = ArrayList(favs + contacts),
                        headers = arrayOf("★") + headers.keys,
                        headersCounts = arrayOf(favs.size) + headers.values
                    )
                }
            }
            return ListBundle(
                items = contacts,
                headers = headers.keys.toTypedArray(),
                headersCounts = headers.values.toTypedArray()
            )
        }

        fun fromRecents(recents: ArrayList<Recent>): ListBundle<Recent> {
            if (recents.isEmpty()) {
                return ListBundle()
            }
            val headers = recents.groupingBy { getRelativeDateString(it.date) }.eachCount()
            return ListBundle(
                items = recents,
                headers = headers.keys.toTypedArray(),
                headersCounts = headers.values.toTypedArray()
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