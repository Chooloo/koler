package com.chooloo.www.chooloolib.domain.model

import android.net.Uri
import com.chooloo.www.chooloolib.domain.model.record.ContactRecord

data class ContactData(
    val id: Long,
    var name: String?,
    var imageUri: Uri? = null,
    var number: String? = null,
    var isFavorite: Boolean = false
) {
    companion object {
        val UNKNOWN = ContactData(0, name = "Unknown")

        fun fromRecord(record: ContactRecord?, number: String? = null) = record?.let {
            ContactData(
                id = record.id,
                number = number,
                name = record.name,
                isFavorite = record.starred,
                imageUri = record.photoUri?.let { Uri.parse(record.photoUri) }
            )
        } ?: UNKNOWN
    }
}

