package com.chooloo.www.koler.data

data class ContactsBundle(
    val contacts: Array<Contact>,
    val headers: Array<String> = arrayOf(),
    val headersCounts: Array<Int> = arrayOf()
) {
    val listBundle = ListBundle(contacts)

    val listBundleByLetters = ListBundle(
        items = contacts,
        headers = headers,
        headersCounts = headersCounts
    )

    val listBundleByLettersWithFavs: ListBundle<Contact>
        get() {
            val favs = contacts.filter { it.starred }.toTypedArray()
            return if (favs.isNotEmpty()) {
                ListBundle(
                    items = favs + contacts,
                    headers = arrayOf("★") + headers,
                    headersCounts = arrayOf(favs.size) + headersCounts
                )
            } else listBundleByLetters
        }
}
