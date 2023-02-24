package com.chooloo.www.chooloolib.interactor.phoneaccounts

import com.chooloo.www.chooloolib.data.model.PhoneAccount
import com.chooloo.www.chooloolib.data.model.PhoneLookupAccount
import com.chooloo.www.chooloolib.interactor.base.BaseInteractor

interface PhonesInteractor : BaseInteractor<PhonesInteractor.Listener> {
    interface Listener

    suspend fun getContactAccounts(contactId: Long): List<PhoneAccount>
    suspend fun lookupAccount(number: String?): PhoneLookupAccount?
}