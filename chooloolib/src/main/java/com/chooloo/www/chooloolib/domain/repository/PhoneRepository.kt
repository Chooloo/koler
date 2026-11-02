package com.chooloo.www.chooloolib.domain.repository

import com.chooloo.www.chooloolib.domain.model.Phone
import com.chooloo.www.chooloolib.domain.model.PhoneLookup
import kotlinx.coroutines.flow.Flow

interface PhoneRepository {
    fun phones(contactId: Long? = null, filter: String? = null): Flow<List<Phone>>
    suspend fun lookup(number: String): PhoneLookup?
}
