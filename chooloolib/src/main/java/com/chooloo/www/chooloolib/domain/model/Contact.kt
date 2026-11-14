package com.chooloo.www.chooloolib.domain.model

data class Contact(
    val id: Long,
    val name: String?,
    val photoUri: String? = null,
    val number: String? = null,
    val isFavorite: Boolean = false,
)
