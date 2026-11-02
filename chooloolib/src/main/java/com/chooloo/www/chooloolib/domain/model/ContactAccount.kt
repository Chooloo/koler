package com.chooloo.www.chooloolib.domain.model

data class ContactAccount(
    val id: Long,
    val contactId: Long,
    val type: Type,
    val data: String? = null,
    val label: String? = null,
    val accountName: String? = null,
    val accountType: String? = null,
) {
    enum class Type { IM, CUSTOM, PHONE, EMAIL, NAME, ORGANIZATION, WHATSAPP }
}
