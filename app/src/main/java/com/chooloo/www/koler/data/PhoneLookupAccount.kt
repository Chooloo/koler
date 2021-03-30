package com.chooloo.www.koler.data

import PhoneAccount.PhoneAccountType

data class PhoneLookupAccount(
    val name: String?,
    val number: String?,
    val contactId: Long? = null,
    val photoUri: String? = null,
    val starred: Boolean? = false,
    val type: PhoneAccountType = PhoneAccountType.OTHER
)
