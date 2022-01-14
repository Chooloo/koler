package com.chooloo.www.chooloolib.di.livedatafactory

import com.chooloo.www.chooloolib.livedata.ContactsProviderLiveData
import com.chooloo.www.chooloolib.livedata.PhonesProviderLiveData
import com.chooloo.www.chooloolib.livedata.RecentsProviderLiveData

interface LiveDataFactory {
    fun allocRecentsProviderLiveData(): RecentsProviderLiveData
    fun allocContactsProviderLiveData(): ContactsProviderLiveData
    fun allocPhonesProviderLiveData(contactId: Long? = null): PhonesProviderLiveData
}
