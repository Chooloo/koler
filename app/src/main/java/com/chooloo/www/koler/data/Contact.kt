package com.chooloo.www.koler.data

data class Contact(
    val id: Long = 0,
    val name: String? = null,
    val photoUri: String? = null,
    val starred: Boolean = false,
    val lookupKey: String? = null,
) {
    override fun toString() = "Contact with id:$id name:$name"

    companion object {
        val UNKNOWN = Contact(name = "Unknown")
        val VOICEMAIL = Contact(name = "Voicemail")
        val PRIVATE = Contact(name = "Private Number")
    }
}

