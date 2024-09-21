package com.chooloo.www.chooloolib.domain.model.record

import android.provider.ContactsContract.CommonDataKinds.Phone

data class PhoneLookupRecord(
    val name: String?,
    val label: String? = null,
    val number: String? = null,
    val contactId: Long? = null,
    val photoUri: String? = null,
    val starred: Boolean? = false,
    val type: Int = Phone.TYPE_OTHER
) {
    val displayString: String
        get() = name ?: (number ?: "Unknown")

    companion object {
        val PRIVATE = PhoneLookupRecord("Private Number")
    }
}
