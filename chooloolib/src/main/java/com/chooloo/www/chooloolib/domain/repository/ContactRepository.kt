package com.chooloo.www.chooloolib.domain.repository

import com.chooloo.www.chooloolib.domain.model.Contact
import kotlinx.coroutines.flow.Flow

interface ContactRepository {
    fun contacts(filter: String? = null): Flow<List<Contact>>
    fun contact(contactId: Long): Flow<Contact?>
    suspend fun contactIdOf(link: String): Long?
    suspend fun deleteContact(contactId: Long)
    suspend fun setFavorite(contactId: Long, isFavorite: Boolean)
}
