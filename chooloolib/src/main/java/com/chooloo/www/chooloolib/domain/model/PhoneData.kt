package com.chooloo.www.chooloolib.domain.model

import android.provider.ContactsContract

data class PhoneData(
    val number: String,
    val label: String? = null,
    val normalizedNumber: String? = null,
    val type: Int = ContactsContract.CommonDataKinds.Phone.TYPE_OTHER
)