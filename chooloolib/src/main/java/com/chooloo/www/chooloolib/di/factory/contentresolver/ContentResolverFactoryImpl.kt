package com.chooloo.www.chooloolib.di.factory.contentresolver

import android.content.Context
import com.chooloo.www.chooloolib.contentresolver.*
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContentResolverFactoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : ContentResolverFactory {
    override fun getRecentsContentResolver(recentId: Long?) =
        RecentsContentResolver(context, recentId)

    override fun getPhonesContentResolver(contactId: Long?) =
        PhonesContentResolver(context, contactId)

    override fun getContactsContentResolver(contactId: Long?) =
        ContactsContentResolver(context, contactId)

    override fun getRawContactsContentResolver(contactId: Long) =
        RawContactsContentResolver(context, contactId)

    override fun getPhoneLookupContentResolver(number: String?) =
        PhoneLookupContentResolver(context, number)
}