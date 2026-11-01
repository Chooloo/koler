package com.chooloo.www.chooloolib.domain.model

import android.provider.ContactsContract

data class PhoneData(
    val id: Long = 0,
    val number: String,
    val label: String? = null,
    val isPrimary: Boolean = false,
    val normalizedNumber: String? = null,
    val type: Int = ContactsContract.CommonDataKinds.Phone.TYPE_OTHER
)