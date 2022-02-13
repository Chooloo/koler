package com.chooloo.www.chooloolib.repository.contacts

import androidx.lifecycle.LiveData
import com.chooloo.www.chooloolib.model.ContactAccount

interface ContactsRepository {
    fun getContacts(): LiveData<List<ContactAccount>>
    fun getContact(contactId: Long, callback: (ContactAccount?) -> Unit)
}