package com.chooloo.www.chooloolib.data.repository.rawcontacts

import com.chooloo.www.chooloolib.di.factory.contentresolver.ContentResolverFactory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RawContactsRepositoryImpl @Inject constructor(
    private val contentResolverFactory: ContentResolverFactory
) : RawContactsRepository {
    override fun getRawContacts(contactId: Long) =
        contentResolverFactory.getRawContactsContentResolver(contactId).getItemsFlow()
}