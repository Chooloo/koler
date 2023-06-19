package com.chooloo.www.chooloolib.data.model

data class ContactAccount(
    val id: Long = 0,
    val name: String? = null,
    val photoUri: String? = null,
    val starred: Boolean = false,
    val lookupKey: String? = null,
) {
    override fun toString() = "Contact with id:$id name:$name"

    override fun equals(other: Any?) = other is ContactAccount && id == other.id
    override fun hashCode(): Int {
        return id.hashCode()
    }
}

