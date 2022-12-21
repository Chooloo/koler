package com.chooloo.www.chooloolib.data.repository.phones

import com.chooloo.www.chooloolib.di.factory.contentresolver.ContentResolverFactory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PhonesRepositoryImpl @Inject constructor(
    private val contentResolverFactory: ContentResolverFactory
) : PhonesRepository {
    override fun getPhones(contactId: Long?) =
        contentResolverFactory.getPhonesContentResolver(contactId).getItemsFlow()
}