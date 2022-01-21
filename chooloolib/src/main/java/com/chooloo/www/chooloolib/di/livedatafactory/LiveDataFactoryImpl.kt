package com.chooloo.www.chooloolib.di.livedatafactory

import android.content.Context
import com.chooloo.www.chooloolib.livedata.ContactsProviderLiveData
import com.chooloo.www.chooloolib.livedata.PhonesProviderLiveData
import com.chooloo.www.chooloolib.livedata.RecentsProviderLiveData

class LiveDataFactoryImpl(private val context: Context) : LiveDataFactory {
    override fun allocRecentsProviderLiveData() =
        RecentsProviderLiveData(context)

    override fun allocContactsProviderLiveData() =
        ContactsProviderLiveData(context)

    override fun allocPhonesProviderLiveData(contactId: Long?) =
        PhonesProviderLiveData(context, contactId)
}