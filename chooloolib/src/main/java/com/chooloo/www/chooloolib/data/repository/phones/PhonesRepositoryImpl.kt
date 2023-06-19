package com.chooloo.www.chooloolib.data.repository.phones

import com.chooloo.www.chooloolib.data.model.PhoneAccount
import com.chooloo.www.chooloolib.di.factory.contentresolver.ContentResolverFactory
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PhonesRepositoryImpl @Inject constructor(
    private val contentResolverFactory: ContentResolverFactory
) : PhonesRepository {
    override fun getPhones(contactId: Long?, filter: String?): Flow<List<PhoneAccount>> =
        contentResolverFactory.getPhonesContentResolver(contactId).apply {
            this.filter = filter
        }.getItemsFlow()
}