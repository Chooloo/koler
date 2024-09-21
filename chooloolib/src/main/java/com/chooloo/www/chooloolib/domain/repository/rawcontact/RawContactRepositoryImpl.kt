package com.chooloo.www.chooloolib.domain.repository.rawcontact

import com.chooloo.www.chooloolib.di.factory.contentresolver.ContentResolverFactory
import com.chooloo.www.chooloolib.domain.model.record.RawContactRecord
import com.chooloo.www.chooloolib.domain.repository.base.BaseRepositoryImpl
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RawContactRepositoryImpl @Inject constructor(
    private val contentResolverFactory: ContentResolverFactory
) : BaseRepositoryImpl(), RawContactRepository {
    override fun getRawContacts(contactId: Long, filter: String?): Flow<List<RawContactRecord>> =
        contentResolverFactory.getRawContactsContentResolver(contactId = contactId, filter = filter)
            .getItemsFlow()
}