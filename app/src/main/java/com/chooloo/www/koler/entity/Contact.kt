package com.chooloo.www.koler.entity

import java.io.Serializable

data class Contact(
    var id: Long = 0,
    var name: String? = null,
    var number: String? = null,
    var photoUri: String? = null,
    var starred: Boolean = false,
    var lookupKey: String? = null,
) : Serializable {

    companion object {
        val UNKNOWN = Contact(name = "Unknown")
        val VOICEMAIL = Contact(name = "Voicemail")
        val PRIVATE = Contact(name = "Private Number")
    }

    override fun toString() = "Contact with id:$id name:$name"
    
    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + (name?.hashCode() ?: 0)
        result = 31 * result + (number?.hashCode() ?: 0)
        result = 31 * result + (photoUri?.hashCode() ?: 0)
        result = 31 * result + starred.hashCode()
        return result
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Contact

        if (id != other.id) return false
        if (name != other.name) return false
        if (number != other.number) return false
        if (photoUri != other.photoUri) return false
        if (starred != other.starred) return false
        if (lookupKey != other.lookupKey) return false

        return true
    }
}

