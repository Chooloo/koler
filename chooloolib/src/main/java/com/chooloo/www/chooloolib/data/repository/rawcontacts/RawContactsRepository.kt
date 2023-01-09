package com.chooloo.www.chooloolib.data.repository.rawcontacts

import com.chooloo.www.chooloolib.data.model.RawContactAccount
import kotlinx.coroutines.flow.Flow

interface RawContactsRepository {
    fun getRawContacts(contactId: Long, filter: String? = null): Flow<List<RawContactAccount>>
}