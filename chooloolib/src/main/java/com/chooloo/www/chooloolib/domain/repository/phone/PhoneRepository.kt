package com.chooloo.www.chooloolib.domain.repository.phone

import com.chooloo.www.chooloolib.domain.model.record.PhoneLookupRecord
import com.chooloo.www.chooloolib.domain.model.record.PhoneRecord
import com.chooloo.www.chooloolib.domain.repository.base.BaseRepository
import kotlinx.coroutines.flow.Flow

interface PhoneRepository : BaseRepository {
    suspend fun getPhones(contactId: Long? = null, filter: String? = null): List<PhoneRecord>
    fun getPhonesFlow(contactId: Long? = null, filter: String? = null): Flow<List<PhoneRecord>>

    suspend fun lookupAccount(number: String?): PhoneLookupRecord?
    suspend fun getContactAccounts(contactId: Long): List<PhoneRecord>
    fun getContactAccountsFlow(contactId: Long): Flow<List<PhoneRecord>>
}