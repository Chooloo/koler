package com.chooloo.www.chooloolib.repository.phones

import com.chooloo.www.chooloolib.di.factory.livedata.LiveDataFactory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PhonesRepositoryImpl @Inject constructor(
    private val liveDataFactory: LiveDataFactory
) : PhonesRepository {
    override fun getPhones(contactId: Long?) = liveDataFactory.getPhonesLiveData(contactId)
}