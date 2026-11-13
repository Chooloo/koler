package com.chooloo.www.chooloolib.domain.model

data class ContactDetails(
    val emails: List<String> = emptyList(),
    val organization: String? = null,
    val whatsappNumber: String? = null,
)
