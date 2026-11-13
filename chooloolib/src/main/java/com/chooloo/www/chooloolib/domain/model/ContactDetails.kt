package com.chooloo.www.chooloolib.domain.model

data class ContactDetails(
    val phones: List<Phone> = emptyList(),
    val emails: List<String> = emptyList(),
    val organization: String? = null,
    val whatsappNumber: String? = null,
)
