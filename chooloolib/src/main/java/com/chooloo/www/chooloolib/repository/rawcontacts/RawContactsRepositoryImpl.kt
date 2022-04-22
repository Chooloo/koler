package com.chooloo.www.chooloolib.repository.rawcontacts

import com.chooloo.www.chooloolib.di.factory.livedata.LiveDataFactory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RawContactsRepositoryImpl @Inject constructor(
    private val liveDataFactory: LiveDataFactory
) : RawContactsRepository {
    override fun getRawContacts(contactId: Long) = liveDataFactory.getRawContactsLiveData(contactId)
}