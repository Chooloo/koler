package com.chooloo.www.callmanager.entity

import com.chooloo.www.callmanager.util.Utilities
import java.io.Serializable

class Contact(
        var contactId: Long? = 0,
        var name: String? = null,
        var number: String? = null,
        var photoUri: String? = null,
        var starred: Boolean = false
) : Serializable {
    companion object {
        @JvmField val UNKNOWN = Contact(name = "Unknown")
        @JvmField val VOICEMAIL = Contact(name = "Voicemail")
        @JvmField val PRIVATE = Contact(name = "Private Number")
    }

    override fun toString(): String {
        return String.format(Utilities.sLocale, "id: %d, name: %s", contactId, name)
    }

    override fun equals(obj: Any?): Boolean {
        return obj is Contact && (super.equals(obj) || name == obj.name)
    }
}

