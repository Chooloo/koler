package com.chooloo.www.chooloolib.domain.model

data class Phone(
    val id: Long,
    val contactId: Long,
    val number: String,
    val displayName: String? = null,
    val label: String? = null,
    val type: Int = 0,
    val photoUri: String? = null,
    val isFavorite: Boolean = false,
    val isPrimary: Boolean = false,
)
