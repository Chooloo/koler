package com.chooloo.www.koler.interactor.phoneaccounts

import PhoneAccount
import com.chooloo.www.koler.data.PhoneLookupAccount
import com.chooloo.www.koler.interactor.base.BaseInteractor

interface PhoneAccountsInteractor : BaseInteractor<PhoneAccountsInteractor.Listener> {
    interface Listener

    fun lookupAccount(number: String): PhoneLookupAccount?
    fun getContactAccounts(contactId: Long): Array<PhoneAccount>
}