package com.chooloo.www.chooloolib.interactor.rawcontacts

import com.chooloo.www.chooloolib.data.repository.rawcontacts.RawContactsRepository
import com.chooloo.www.chooloolib.interactor.base.BaseInteractorImpl
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RawContactsInteractorImpl @Inject constructor(
    private val rawContactsRepository: RawContactsRepository
) :
    BaseInteractorImpl<RawContactsInteractor.Listener>(),
    RawContactsInteractor {

    override fun getRawContacts(contactId: Long) = rawContactsRepository.getRawContacts(contactId)
}