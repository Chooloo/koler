package com.chooloo.www.koler.entity

data class ContactsBundle(
    val contacts: Array<Contact>,
    val headers: Array<String> = arrayOf(),
    val headersCounts: Array<Int> = arrayOf()
) {

    val contactsWithFavs: ContactsBundle
        get() {
            val favs = contacts.filter { it.starred }.toTypedArray()
            return ContactsBundle(
                contacts = favs + contacts,
                headers = arrayOf("★") + headers,
                headersCounts = arrayOf(favs.size) + headersCounts
            )
        }
}
