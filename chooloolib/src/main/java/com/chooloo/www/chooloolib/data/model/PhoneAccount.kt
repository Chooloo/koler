package com.chooloo.www.chooloolib.data.model

import android.provider.ContactsContract.CommonDataKinds.Phone

data class PhoneAccount(
    val number: String,
    val contactId: Long,
    val displayName: String,
    val label: String? = null,
    val normalizedNumber: String?,
    val type: Int = Phone.TYPE_OTHER
) {
    val cleanNumber get() = number.replace("-", "")
}
