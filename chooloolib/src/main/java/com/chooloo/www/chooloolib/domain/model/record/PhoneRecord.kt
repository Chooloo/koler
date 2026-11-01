package com.chooloo.www.chooloolib.domain.model.record

import android.provider.ContactsContract.CommonDataKinds.Phone

data class PhoneRecord(
    val id: Long = 0,
    val number: String,
    val contactId: Long,
    val displayName: String,
    val label: String? = null,
    val photoUri: String? = null,
    val normalizedNumber: String?,
    val starred: Boolean = false,
    val isPrimary: Boolean = false,
    val type: Int = Phone.TYPE_OTHER
) {
    val cleanNumber get() = number.replace("-", "")
}
