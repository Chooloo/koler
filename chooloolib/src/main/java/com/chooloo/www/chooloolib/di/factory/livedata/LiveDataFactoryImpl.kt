package com.chooloo.www.chooloolib.di.factory.livedata

import android.content.Context
import com.chooloo.www.chooloolib.di.factory.contentresolver.ContentResolverFactory
import com.chooloo.www.chooloolib.livedata.ContactsLiveData
import com.chooloo.www.chooloolib.livedata.PhonesLiveData
import com.chooloo.www.chooloolib.livedata.RawContactsLiveData
import com.chooloo.www.chooloolib.livedata.RecentsLiveData
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LiveDataFactoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val contentResolverFactory: ContentResolverFactory
) : LiveDataFactory {
    override fun getRecentsLiveData(recentId: Long?) =
        RecentsLiveData(context, recentId, contentResolverFactory)

    override fun getRawContactsLiveData(contactId: Long) =
        RawContactsLiveData(context, contactId, contentResolverFactory)

    override fun getPhonesLiveData(contactId: Long?) =
        PhonesLiveData(context, contactId, contentResolverFactory)

    override fun getContactsLiveData(contactId: Long?) =
        ContactsLiveData(context, contactId, contentResolverFactory)
}