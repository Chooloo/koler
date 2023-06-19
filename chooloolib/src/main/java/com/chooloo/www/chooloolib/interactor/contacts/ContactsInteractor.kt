package com.chooloo.www.chooloolib.interactor.contacts

import com.chooloo.www.chooloolib.data.model.ContactAccount
import com.chooloo.www.chooloolib.interactor.base.BaseInteractor
import kotlinx.coroutines.flow.Flow

interface ContactsInteractor : BaseInteractor<ContactsInteractor.Listener> {
    interface Listener

    fun deleteContact(contactId: Long)
    fun getContact(contactId: Long): Flow<ContactAccount?>
    fun getContacts(): Flow<List<ContactAccount>>
    fun toggleContactFavorite(contactId: Long, isFavorite: Boolean)
    suspend fun blockContact(contactId: Long)
    suspend fun unblockContact(contactId: Long)
    suspend fun getIsContactBlocked(contactId: Long): Boolean
}