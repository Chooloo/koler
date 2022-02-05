package com.chooloo.www.chooloolib.di.factory.livedata

import com.chooloo.www.chooloolib.di.factory.contentresolver.ContentResolverFactory
import com.chooloo.www.chooloolib.livedata.ContactsProviderLiveData
import com.chooloo.www.chooloolib.livedata.PhonesProviderLiveData
import com.chooloo.www.chooloolib.livedata.RecentsProviderLiveData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LiveDataFactoryImpl @Inject constructor(
    private val contentResolverFactory: ContentResolverFactory
) : LiveDataFactory {
    override fun allocRecentsProviderLiveData() =
        RecentsProviderLiveData(contentResolverFactory)

    override fun allocContactsProviderLiveData() =
        ContactsProviderLiveData(contentResolverFactory)

    override fun allocPhonesProviderLiveData(contactId: Long?) =
        PhonesProviderLiveData(contactId, contentResolverFactory)
}