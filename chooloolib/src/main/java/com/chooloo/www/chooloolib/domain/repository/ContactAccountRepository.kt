package com.chooloo.www.chooloolib.domain.repository

import com.chooloo.www.chooloolib.domain.model.ContactAccount
import kotlinx.coroutines.flow.Flow

interface ContactAccountRepository {
    fun accounts(contactId: Long): Flow<List<ContactAccount>>
}
