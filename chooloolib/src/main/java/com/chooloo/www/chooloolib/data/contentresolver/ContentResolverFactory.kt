package com.chooloo.www.chooloolib.data.contentresolver

import android.content.ContentResolver
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContentResolverFactory @Inject constructor(
    private val contentResolver: ContentResolver
) {
    fun getRecentsContentResolver(
        recentId: Long? = null,
        filter: String? = null
    ) = RecentsContentResolver(
        filter = filter,
        recentId = recentId,
        contentResolver = contentResolver,
    )

    fun getPhonesContentResolver(
        filter: String? = null,
        contactId: Long? = null
    ) = PhonesContentResolver(
        filter = filter,
        contactId = contactId,
        contentResolver = contentResolver
    )

    fun getContactsContentResolver(
        filter: String? = null,
        contactId: Long? = null
    ) = ContactsContentResolver(
        filter = filter,
        contactId = contactId,
        contentResolver = contentResolver
    )

    fun getRawContactsContentResolver(
        contactId: Long,
        filter: String? = null
    ) = RawContactsContentResolver(
        filter = filter,
        contactId = contactId,
        contentResolver = contentResolver
    )

    fun getPhoneLookupContentResolver(
        number: String? = null,
        filter: String? = null
    ) = PhoneLookupContentResolver(
        filter = filter,
        number = number,
        contentResolver = contentResolver
    )

    fun getBlockedNumbersContentResolver(
        filter: String? = null
    ) = BlockedNumbersContentResolver(
        filter = filter,
        contentResolver = contentResolver
    )
}
