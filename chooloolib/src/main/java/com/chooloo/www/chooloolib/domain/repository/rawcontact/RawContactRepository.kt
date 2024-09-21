package com.chooloo.www.chooloolib.domain.repository.rawcontact

import com.chooloo.www.chooloolib.domain.model.record.RawContactRecord
import com.chooloo.www.chooloolib.domain.repository.base.BaseRepository
import kotlinx.coroutines.flow.Flow

interface RawContactRepository : BaseRepository {
    fun getRawContacts(contactId: Long, filter: String? = null): Flow<List<RawContactRecord>>
}