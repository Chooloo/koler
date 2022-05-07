package com.chooloo.www.chooloolib.interactor.rawcontacts

import com.chooloo.www.chooloolib.interactor.base.BaseInteractor
import com.chooloo.www.chooloolib.model.RawContactAccount

interface RawContactsInteractor : BaseInteractor<RawContactsInteractor.Listener> {
    interface Listener

    fun queryRawContacts(contactId: Long, callback: (List<RawContactAccount>) -> Unit)
}