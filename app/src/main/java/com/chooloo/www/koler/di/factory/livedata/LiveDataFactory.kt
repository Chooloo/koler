package com.chooloo.www.koler.di.factory.livedata

import com.chooloo.www.koler.livedata.ContactsProviderLiveData
import com.chooloo.www.koler.livedata.PhonesProviderLiveData
import com.chooloo.www.koler.livedata.RecentsProviderLiveData

interface LiveDataFactory {
    fun allocRecentsProviderLiveData(): RecentsProviderLiveData
    fun allocContactsProviderLiveData(): ContactsProviderLiveData
    fun allocPhonesProviderLiveData(contactId: Long? = null): PhonesProviderLiveData
}
