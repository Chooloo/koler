package com.chooloo.www.chooloolib.data.repository.contacts

import com.chooloo.www.chooloolib.data.model.ContactAccount
import kotlinx.coroutines.flow.Flow

interface ContactsRepository {
    fun getContacts(): Flow<List<ContactAccount>>
    fun getContact(contactId: Long): Flow<ContactAccount?>
}