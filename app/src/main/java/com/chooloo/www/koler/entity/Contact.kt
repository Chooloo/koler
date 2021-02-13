package com.chooloo.www.koler.entity

import java.io.Serializable

data class Contact(
        var contactId: Long = 0,
        var name: String? = null,
        var number: String? = null,
        var photoUri: String? = null,
        var starred: Boolean = false,
        var lookupKey: String? = null,
) : Serializable {

    companion object {
        @JvmField val UNKNOWN = Contact(name = "Unknown")
        @JvmField val VOICEMAIL = Contact(name = "Voicemail")
        @JvmField val PRIVATE = Contact(name = "Private Number")
    }

    override fun toString(): String {
        return "Contact with id:$contactId name:$name"
    }

    override fun equals(other: Any?): Boolean {
        return other is Contact && (super.equals(other) || name == other.name)
    }

    override fun hashCode(): Int {
        var result = contactId.hashCode()
        result = 31 * result + (name?.hashCode() ?: 0)
        result = 31 * result + (number?.hashCode() ?: 0)
        result = 31 * result + (photoUri?.hashCode() ?: 0)
        result = 31 * result + starred.hashCode()
        return result
    }
}

