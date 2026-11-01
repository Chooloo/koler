package com.chooloo.www.chooloolib.domain.model.record

data class BlockedNumberRecord(
    val id: Long = 0,
    val number: String,
    val e164Number: String? = null,
)
