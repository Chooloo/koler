package com.chooloo.www.chooloolib.data.repository.contacts

import com.chooloo.www.chooloolib.data.model.ContactAccount
import com.chooloo.www.chooloolib.di.factory.contentresolver.ContentResolverFactory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContactsRepositoryImpl @Inject constructor(
    private val contentResolverFactory: ContentResolverFactory
) : ContactsRepository {
    override fun getContacts() =
        contentResolverFactory.getContactsContentResolver().getItemsFlow()

    override fun getContact(contactId: Long): Flow<ContactAccount?> = flow {
        contentResolverFactory.getContactsContentResolver(contactId).getItemsFlow().collect {
            emit(it.getOrNull(0))
        }
    }
}