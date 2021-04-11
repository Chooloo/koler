package com.chooloo.www.koler.data

import android.provider.ContactsContract
import android.provider.ContactsContract.CommonDataKinds.Phone

data class PhoneLookupAccount(
    val name: String?,
    val number: String?,
    val contactId: Long? = null,
    val photoUri: String? = null,
    val starred: Boolean? = false,
    val type: Int = Phone.TYPE_OTHER
)
