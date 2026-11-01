package com.chooloo.www.chooloolib.domain.model.record

data class ContactRecord(
    val id: Long = 0,
    val name: String? = null,
    val photoUri: String? = null,
    val starred: Boolean = false,
    val lookupKey: String? = null,
    val hasPhoneNumber: Boolean = false,
)
