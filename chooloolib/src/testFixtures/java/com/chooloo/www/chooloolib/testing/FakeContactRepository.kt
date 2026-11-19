package com.chooloo.www.chooloolib.testing

import com.chooloo.www.chooloolib.domain.model.Contact
import com.chooloo.www.chooloolib.domain.model.ContactDetails
import com.chooloo.www.chooloolib.domain.repository.ContactRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeContactRepository : ContactRepository {
    override fun contacts(filter: String): Flow<List<Contact>> = flowOf(emptyList())

    override fun contact(contactId: Long): Flow<Contact?> = flowOf(null)

    override fun details(contactId: Long): Flow<ContactDetails> = flowOf(ContactDetails())

    override suspend fun contactIdOf(link: String): Long? = null

    override suspend fun deleteContact(contactId: Long) = Unit

    override suspend fun setFavorite(contactId: Long, isFavorite: Boolean) = Unit
}
