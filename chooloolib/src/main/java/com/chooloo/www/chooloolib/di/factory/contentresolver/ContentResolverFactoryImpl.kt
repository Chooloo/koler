package com.chooloo.www.chooloolib.di.factory.contentresolver

import android.content.Context
import com.chooloo.www.chooloolib.contentresolver.ContactsContentResolver
import com.chooloo.www.chooloolib.contentresolver.PhoneLookupContentResolver
import com.chooloo.www.chooloolib.contentresolver.PhonesContentResolver
import com.chooloo.www.chooloolib.contentresolver.RecentsContentResolver
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ContentResolverFactoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : ContentResolverFactory {
    override fun getPhoneLookupContentResolver(number: String?) =
        PhoneLookupContentResolver(context, number)

    override fun getRecentsContentResolver(recentId: Long?) =
        RecentsContentResolver(context, recentId)

    override fun getPhonesContentResolver(contactId: Long?) =
        PhonesContentResolver(context, contactId)

    override fun getContactsContentResolver(contactId: Long?) =
        ContactsContentResolver(context, contactId)
}