package com.chooloo.www.koler.entity

data class ContactsBundle(
    val contacts: Array<Contact>,
    val headers: Array<String> = arrayOf(),
    val headersCounts: Array<Int> = arrayOf()
)
