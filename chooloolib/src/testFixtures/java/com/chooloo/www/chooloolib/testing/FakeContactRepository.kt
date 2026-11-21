package com.chooloo.www.chooloolib.testing

import com.chooloo.www.chooloolib.domain.enum.ContactOrder
import com.chooloo.www.chooloolib.domain.model.Contact
import com.chooloo.www.chooloolib.domain.model.ContactDetails
import com.chooloo.www.chooloolib.domain.repository.ContactRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeContactRepository(
    private val contacts: List<Contact> = emptyList(),
    private val details: Map<Long, ContactDetails> = emptyMap(),
) : ContactRepository {
    override fun contacts(filter: String, order: ContactOrder): Flow<List<Contact>> =
        flowOf(contacts)

    override fun contact(contactId: Long): Flow<Contact?> =
        flowOf(contacts.firstOrNull { it.id == contactId })

    override fun details(contactId: Long): Flow<ContactDetails> =
        flowOf(details[contactId] ?: ContactDetails())

    override suspend fun contactIdOf(link: String): Long? = null

    override suspend fun deleteContact(contactId: Long) = Unit

    override suspend fun setFavorite(contactId: Long, isFavorite: Boolean) = Unit
}
