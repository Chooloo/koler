package com.chooloo.www.koler.interactor.phoneaccounts

import com.chooloo.www.koler.data.account.PhoneAccount
import android.content.Context
import com.chooloo.www.koler.contentresolver.PhoneContentResolver
import com.chooloo.www.koler.contentresolver.PhoneLookupContentResolver
import com.chooloo.www.koler.data.account.PhoneLookupAccount
import com.chooloo.www.koler.interactor.base.BaseInteractorImpl

class PhoneAccountsInteractorImpl(
    private val context: Context
) : BaseInteractorImpl<PhoneAccountsInteractor.Listener>(), PhoneAccountsInteractor {
    override fun lookupAccount(number: String): PhoneLookupAccount? =
        PhoneLookupContentResolver(context, number).content.getOrNull(0)

    override fun getContactAccounts(contactId: Long): Array<PhoneAccount> =
        PhoneContentResolver(context, contactId).content.toTypedArray()
}