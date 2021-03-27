package com.chooloo.www.koler.data

import android.os.Parcel
import android.os.Parcelable

data class Contact(
    var id: Long = 0,
    var name: String? = null,
    var number: String? = null,
    var photoUri: String? = null,
    var starred: Boolean = false,
    var lookupKey: String? = null,
) : Parcelable {

    companion object CREATOR : Parcelable.Creator<Contact> {
        val UNKNOWN = Contact(name = "Unknown")
        val VOICEMAIL = Contact(name = "Voicemail")
        val PRIVATE = Contact(name = "Private Number")

        override fun createFromParcel(parcel: Parcel) = Contact(parcel)
        override fun newArray(size: Int): Array<Contact?> = arrayOfNulls(size)
    }

    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readByte() != 0.toByte(),
        parcel.readString()
    )

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

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(name)
        parcel.writeString(number)
        parcel.writeString(photoUri)
        parcel.writeByte(if (starred) 1 else 0)
        parcel.writeString(lookupKey)
    }

    override fun describeContents() = 0
}

