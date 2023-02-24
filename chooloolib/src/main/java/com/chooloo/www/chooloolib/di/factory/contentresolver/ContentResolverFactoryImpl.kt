package com.chooloo.www.chooloolib.di.factory.contentresolver

import android.content.ContentResolver
import com.chooloo.www.chooloolib.data.contentresolver.*
import com.chooloo.www.chooloolib.di.module.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContentResolverFactoryImpl @Inject constructor(
    private val contentResolver: ContentResolver,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ContentResolverFactory {
    override fun getRecentsContentResolver(recentId: Long?) =
        RecentsContentResolver(recentId, contentResolver, ioDispatcher)

    override fun getPhonesContentResolver(contactId: Long?) =
        PhonesContentResolver(contactId, contentResolver, ioDispatcher)

    override fun getContactsContentResolver(contactId: Long?) =
        ContactsContentResolver(contactId, contentResolver, ioDispatcher)

    override fun getRawContactsContentResolver(contactId: Long) =
        RawContactsContentResolver(contactId, contentResolver, ioDispatcher)

    override fun getPhoneLookupContentResolver(number: String?) =
        PhoneLookupContentResolver(number, contentResolver, ioDispatcher)
}