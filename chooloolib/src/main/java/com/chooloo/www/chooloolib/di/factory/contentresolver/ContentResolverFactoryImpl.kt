package com.chooloo.www.chooloolib.di.factory.contentresolver

import android.content.ContentResolver
import com.chooloo.www.chooloolib.domain.contentresolver.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContentResolverFactoryImpl @Inject constructor(
    private val contentResolver: ContentResolver
) : ContentResolverFactory {
    override fun getRecentsContentResolver(
        recentId: Long?,
        filter: String?
    ) = RecentsContentResolver(
        filter = filter,
        recentId = recentId,
        contentResolver = contentResolver,
    )

    override fun getPhonesContentResolver(
        filter: String?,
        contactId: Long?
    ) = PhonesContentResolver(
        filter = filter,
        contactId = contactId,
        contentResolver = contentResolver
    )

    override fun getContactsContentResolver(
        filter: String?,
        contactId: Long?
    ) = ContactsContentResolver(
        filter = filter,
        contactId = contactId,
        contentResolver = contentResolver
    )

    override fun getRawContactsContentResolver(
        contactId: Long,
        filter: String?
    ) = RawContactsContentResolver(
        filter = filter,
        contactId = contactId,
        contentResolver = contentResolver
    )

    override fun getPhoneLookupContentResolver(
        number: String?,
        filter: String?
    ) = PhoneLookupContentResolver(
        filter = filter,
        number = number,
        contentResolver = contentResolver
    )
}