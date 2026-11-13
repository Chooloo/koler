package com.chooloo.www.chooloolib.domain.repository

import com.chooloo.www.chooloolib.domain.model.ContactDetails
import kotlinx.coroutines.flow.Flow

interface ContactDetailsRepository {
    fun details(contactId: Long): Flow<ContactDetails>
}
