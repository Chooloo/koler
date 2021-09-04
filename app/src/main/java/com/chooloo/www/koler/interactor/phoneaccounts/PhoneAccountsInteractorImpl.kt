package com.chooloo.www.koler.interactor.phoneaccounts

import android.content.Context
import com.chooloo.www.koler.contentresolver.PhoneContentResolver
import com.chooloo.www.koler.contentresolver.PhoneLookupContentResolver
import com.chooloo.www.koler.data.account.PhoneAccount
import com.chooloo.www.koler.data.account.PhoneLookupAccount
import com.chooloo.www.koler.interactor.base.BaseInteractorImpl

class PhoneAccountsInteractorImpl(
    private val context: Context
) : BaseInteractorImpl<PhoneAccountsInteractor.Listener>(), PhoneAccountsInteractor {
    override fun lookupAccount(number: String?, callback: (PhoneLookupAccount) -> Unit) {
        PhoneLookupContentResolver(context, number).queryContent { phones ->
            callback.invoke(phones?.getOrNull(0) ?: PhoneLookupAccount(null, number))
        }
    }

    override fun getContactAccounts(contactId: Long, callback: (Array<PhoneAccount>?) -> Unit) {
        PhoneContentResolver(context, contactId).queryContent {
            callback.invoke(it?.toTypedArray())
        }
    }
}