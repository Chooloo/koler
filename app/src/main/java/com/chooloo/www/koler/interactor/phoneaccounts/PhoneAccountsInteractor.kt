package com.chooloo.www.koler.interactor.phoneaccounts

import com.chooloo.www.koler.data.account.PhoneAccount
import com.chooloo.www.koler.data.account.PhoneLookupAccount
import com.chooloo.www.koler.interactor.base.BaseInteractor

interface PhoneAccountsInteractor : BaseInteractor<PhoneAccountsInteractor.Listener> {
    interface Listener

    fun lookupAccount(number: String): PhoneLookupAccount?
    fun getContactAccounts(contactId: Long): Array<PhoneAccount>
}