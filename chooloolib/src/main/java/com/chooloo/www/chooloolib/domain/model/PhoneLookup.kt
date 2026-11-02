package com.chooloo.www.chooloolib.domain.model

data class PhoneLookup(
    val contactId: Long,
    val name: String?,
    val number: String?,
    val label: String? = null,
    val type: Int = 0,
    val photoUri: String? = null,
)
