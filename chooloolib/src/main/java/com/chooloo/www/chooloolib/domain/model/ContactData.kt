package com.chooloo.www.chooloolib.domain.model

import android.net.Uri
import com.chooloo.www.chooloolib.domain.model.record.ContactRecord

data class ContactData(
    val id: Long,
    val name: String?,
    val imageUri: Uri? = null,
    val number: String? = null,
    val isFavorite: Boolean = false,
    val hasPhoneNumber: Boolean = false
) {
    companion object {
        val UNKNOWN = ContactData(0, name = "Unknown")

        fun fromRecord(record: ContactRecord?, number: String? = null) = record?.let {
            ContactData(
                id = record.id,
                number = number,
                name = record.name,
                isFavorite = record.starred,
                hasPhoneNumber = record.hasPhoneNumber,
                imageUri = record.photoUri?.let(Uri::parse)
            )
        } ?: UNKNOWN
    }
}
