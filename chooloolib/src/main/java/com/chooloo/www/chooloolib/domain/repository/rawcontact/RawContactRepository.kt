package com.chooloo.www.chooloolib.domain.repository.rawcontact

import com.chooloo.www.chooloolib.domain.model.record.RawContactRecord
import kotlinx.coroutines.flow.Flow

interface RawContactRepository {
    fun getRawContacts(contactId: Long, filter: String? = null): Flow<List<RawContactRecord>>
}
