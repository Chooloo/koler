package com.chooloo.www.chooloolib.data.repository.rawcontacts

import com.chooloo.www.chooloolib.data.model.RawContactAccount
import com.chooloo.www.chooloolib.di.factory.contentresolver.ContentResolverFactory
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RawContactsRepositoryImpl @Inject constructor(
    private val contentResolverFactory: ContentResolverFactory
) : RawContactsRepository {
    override fun getRawContacts(contactId: Long, filter: String?): Flow<List<RawContactAccount>> =
        contentResolverFactory.getRawContactsContentResolver(contactId).apply {
            this.filter = filter
        }.getItemsFlow()
}