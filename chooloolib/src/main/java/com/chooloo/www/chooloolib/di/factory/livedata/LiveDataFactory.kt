package com.chooloo.www.chooloolib.di.factory.livedata

import com.chooloo.www.chooloolib.livedata.ContactsLiveData
import com.chooloo.www.chooloolib.livedata.PhonesLiveData
import com.chooloo.www.chooloolib.livedata.RawContactsLiveData
import com.chooloo.www.chooloolib.livedata.RecentsLiveData

interface LiveDataFactory {
    fun getPhonesLiveData(contactId: Long? = null): PhonesLiveData
    fun getRecentsLiveData(recentId: Long? = null): RecentsLiveData
    fun getRawContactsLiveData(contactId: Long): RawContactsLiveData
    fun getContactsLiveData(contactId: Long? = null): ContactsLiveData
}
