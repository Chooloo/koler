package com.chooloo.www.koler.data

data class ContactsBundle(
    val contacts: ArrayList<Contact>,
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
            val favs = ArrayList(contacts.filter { it.starred })
            return if (favs.isNotEmpty()) {
                ListBundle(
                    items = ArrayList(favs + contacts),
                    headers = arrayOf("★") + headers,
                    headersCounts = arrayOf(favs.size) + headersCounts
                )
            } else listBundleByLetters
        }
}
