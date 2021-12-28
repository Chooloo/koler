package com.chooloo.www.koler.di.livedatafactory

import android.content.Context
import com.chooloo.www.koler.livedata.ContactsProviderLiveData
import com.chooloo.www.koler.livedata.PhonesProviderLiveData
import com.chooloo.www.koler.livedata.RecentsProviderLiveData

class LiveDataFactoryImpl(private val context: Context) : LiveDataFactory {
    override fun allocRecentsProviderLiveData() =
        RecentsProviderLiveData(context)

    override fun allocContactsProviderLiveData() =
        ContactsProviderLiveData(context)

    override fun allocPhonesProviderLiveData(contactId: Long?) =
        PhonesProviderLiveData(context, contactId)
}