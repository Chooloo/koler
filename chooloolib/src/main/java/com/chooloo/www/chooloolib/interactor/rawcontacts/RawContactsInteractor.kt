package com.chooloo.www.chooloolib.interactor.rawcontacts

import com.chooloo.www.chooloolib.data.model.RawContactAccount
import com.chooloo.www.chooloolib.interactor.base.BaseInteractor
import kotlinx.coroutines.flow.Flow

interface RawContactsInteractor : BaseInteractor<RawContactsInteractor.Listener> {
    interface Listener

    fun getRawContacts(contactId: Long): Flow<List<RawContactAccount>>
}