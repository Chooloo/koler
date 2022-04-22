package com.chooloo.www.chooloolib.model

import android.provider.ContactsContract.CommonDataKinds.*
import androidx.annotation.StringRes
import com.chooloo.www.chooloolib.R

data class RawContactAccount(
    val id: Long,
    val data: String?,
    val contactId: Long,
    val type: RawContactType
) {
    enum class RawContactType(
        val contentType: String,
        @StringRes val titleStringRes: Int
    ) {
        IM(Im.CONTENT_ITEM_TYPE, R.string.type_label_im),
        CUSTOM("", R.string.type_label_custom),
        PHONE(Phone.CONTENT_ITEM_TYPE, R.string.type_label_phone),
        EMAIL(Email.CONTENT_ITEM_TYPE, R.string.type_label_email),
        NAME(StructuredName.CONTENT_ITEM_TYPE, R.string.type_label_name),
        ORGANIZATION(Organization.CONTENT_ITEM_TYPE, R.string.type_label_organization),
        WHATSAPP("vnd.android.cursor.item/vnd.com.whatsapp.profile", R.string.type_label_whatsapp);

        companion object {
            fun fromContentType(contentType: String) =
                values().associateBy(RawContactType::contentType).getOrDefault(contentType, PHONE)
        }
    }
}
