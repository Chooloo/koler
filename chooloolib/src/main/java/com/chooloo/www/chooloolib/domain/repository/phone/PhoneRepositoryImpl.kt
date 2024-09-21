package com.chooloo.www.chooloolib.domain.repository.phone

import com.chooloo.www.chooloolib.domain.model.record.PhoneLookupRecord
import com.chooloo.www.chooloolib.domain.model.record.PhoneRecord
import com.chooloo.www.chooloolib.di.factory.contentresolver.ContentResolverFactory
import com.chooloo.www.chooloolib.domain.repository.base.BaseRepositoryImpl
import io.reactivex.exceptions.OnErrorNotImplementedException
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PhoneRepositoryImpl @Inject constructor(
    private val contentResolverFactory: ContentResolverFactory,
) : BaseRepositoryImpl(), PhoneRepository {
    override suspend fun getPhones(contactId: Long?, filter: String?): List<PhoneRecord> =
        contentResolverFactory.getPhonesContentResolver(
            contactId = if (contactId == 0L) null else contactId,
            filter = filter
        ).getItems()

    override fun getPhonesFlow(contactId: Long?, filter: String?): Flow<List<PhoneRecord>> =
        contentResolverFactory.getPhonesContentResolver(
            contactId = if (contactId == 0L) null else contactId,
            filter = filter
        ).getItemsFlow()

    override suspend fun lookupAccount(number: String?): PhoneLookupRecord? =
        if (number.isNullOrEmpty()) {
            PhoneLookupRecord.PRIVATE
        } else try {
            contentResolverFactory.getPhoneLookupContentResolver(number).getItems().getOrNull(0)
        } catch (e: OnErrorNotImplementedException) {
            null
        }

    override suspend fun getContactAccounts(contactId: Long): List<PhoneRecord> =
        contentResolverFactory.getPhonesContentResolver(contactId = contactId).getItems()

    override fun getContactAccountsFlow(contactId: Long): Flow<List<PhoneRecord>> =
        contentResolverFactory.getPhonesContentResolver(contactId = contactId).getItemsFlow()
}