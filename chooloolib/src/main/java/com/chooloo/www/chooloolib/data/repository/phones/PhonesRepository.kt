package com.chooloo.www.chooloolib.data.repository.phones

import com.chooloo.www.chooloolib.data.model.PhoneAccount
import kotlinx.coroutines.flow.Flow

interface PhonesRepository {
    fun getPhones(contactId: Long? = null): Flow<List<PhoneAccount>>
}