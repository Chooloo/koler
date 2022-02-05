package com.chooloo.www.chooloolib.livedata

import com.chooloo.www.chooloolib.contentresolver.ContactsContentResolver
import com.chooloo.www.chooloolib.data.account.ContactAccount
import com.chooloo.www.chooloolib.di.factory.contentresolver.ContentResolverFactory

class ContactsProviderLiveData(
    private val contentResolverFactory: ContentResolverFactory
) : ContentProviderLiveData<ContactsContentResolver, ContactAccount>() {
    override val contentResolver by lazy { contentResolverFactory.getContactsContentResolver() }
}