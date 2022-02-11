package com.chooloo.www.chooloolib.di.factory.livedata

import android.content.Context
import com.chooloo.www.chooloolib.di.factory.contentresolver.ContentResolverFactory
import com.chooloo.www.chooloolib.livedata.contentprovider.ContactsProviderLiveData
import com.chooloo.www.chooloolib.livedata.contentprovider.PhonesProviderLiveData
import com.chooloo.www.chooloolib.livedata.contentprovider.RecentsProviderLiveData
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LiveDataFactoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val contentResolverFactory: ContentResolverFactory
) : LiveDataFactory {
    override fun getRecentsProviderLiveData(recentId: Long?) =
        RecentsProviderLiveData(context, recentId, contentResolverFactory)

    override fun getPhonesProviderLiveData(contactId: Long?) =
        PhonesProviderLiveData(context, contactId, contentResolverFactory)

    override fun getContactsProviderLiveData(contactId: Long?) =
        ContactsProviderLiveData(context, contactId, contentResolverFactory)
}