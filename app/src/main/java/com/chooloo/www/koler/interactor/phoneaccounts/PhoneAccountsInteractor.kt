package com.chooloo.www.koler.interactor.phoneaccounts

import com.chooloo.www.koler.data.account.PhoneAccount
import com.chooloo.www.koler.data.account.PhoneLookupAccount
import com.chooloo.www.koler.interactor.base.BaseInteractor

interface PhoneAccountsInteractor : BaseInteractor<PhoneAccountsInteractor.Listener> {
    interface Listener

    fun lookupAccount(number: String?): PhoneLookupAccount
    fun lookupAccount(number: String?, callback: (PhoneLookupAccount) -> Unit)
    fun getContactAccounts(contactId: Long, callback: (Array<PhoneAccount>?) -> Unit)
}