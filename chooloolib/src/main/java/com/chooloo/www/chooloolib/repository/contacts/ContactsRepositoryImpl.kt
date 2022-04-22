package com.chooloo.www.chooloolib.repository.contacts

import androidx.lifecycle.LiveData
import com.chooloo.www.chooloolib.di.factory.contentresolver.ContentResolverFactory
import com.chooloo.www.chooloolib.di.factory.livedata.LiveDataFactory
import com.chooloo.www.chooloolib.model.ContactAccount
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContactsRepositoryImpl @Inject constructor(
    private val liveDataFactory: LiveDataFactory,
    private val contentResolverFactory: ContentResolverFactory
) : ContactsRepository {
    override fun getContacts(): LiveData<List<ContactAccount>> =
        liveDataFactory.getContactsLiveData()

    override fun getContact(contactId: Long, callback: (ContactAccount?) -> Unit) {
        contentResolverFactory.getContactsContentResolver(contactId).queryItems {
            callback.invoke(it.getOrNull(0))
        }
    }
}