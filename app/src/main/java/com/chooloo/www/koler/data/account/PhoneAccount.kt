package com.chooloo.www.koler.data.account

import android.provider.ContactsContract.CommonDataKinds.Phone

data class PhoneAccount(
    val number: String,
    val contactId: Long,
    val displayName: String,
    val normalizedNumber: String?,
    val type: Int = Phone.TYPE_OTHER
)
