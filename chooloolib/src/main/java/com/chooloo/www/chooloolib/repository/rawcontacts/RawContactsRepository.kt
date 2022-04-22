package com.chooloo.www.chooloolib.repository.rawcontacts

import androidx.lifecycle.LiveData
import com.chooloo.www.chooloolib.model.RawContactAccount

interface RawContactsRepository {
    fun getRawContacts(contactId: Long): LiveData<List<RawContactAccount>>
}