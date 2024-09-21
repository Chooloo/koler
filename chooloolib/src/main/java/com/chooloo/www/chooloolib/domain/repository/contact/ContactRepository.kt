package com.chooloo.www.chooloolib.domain.repository.contact

import com.chooloo.www.chooloolib.domain.model.record.ContactRecord
import com.chooloo.www.chooloolib.domain.repository.base.BaseRepository
import kotlinx.coroutines.flow.Flow

interface ContactRepository : BaseRepository {
    suspend fun getContact(contactId: Long): ContactRecord?
    fun getContactFlow(contactId: Long): Flow<ContactRecord?>
    suspend fun getContacts(filter: String? = null): List<ContactRecord>
    fun getContactsFlow(filter: String? = null): Flow<List<ContactRecord>>

    suspend fun viewContact(contactId: Long)
    suspend fun editContact(contactId: Long)
    suspend fun createContact(number: String)
    suspend fun deleteContact(contactId: Long)
    suspend fun blockContact(contactId: Long)
    suspend fun unblockContact(contactId: Long)
    suspend fun getIsContactBlocked(contactId: Long): Boolean
    suspend fun toggleContactFavorite(contactId: Long, isFavorite: Boolean)
}