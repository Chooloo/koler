package com.chooloo.www.koler.interactor.phoneaccounts

import PhoneAccount
import android.content.Context
import androidx.databinding.BaseObservable
import com.chooloo.www.koler.contentresolver.PhoneContentResolver
import com.chooloo.www.koler.contentresolver.PhoneLookupContentResolver
import com.chooloo.www.koler.data.PhoneLookupAccount

class PhoneAccountsInteractorImpl(
    val context: Context
) : BaseObservable(), PhoneAccountsInteractor {
    override fun lookupAccount(number: String): PhoneLookupAccount? =
        PhoneLookupContentResolver(context, number).content.getOrNull(0)

    override fun getContactAccounts(contactId: Long): Array<PhoneAccount> =
        PhoneContentResolver(context, contactId).content.toTypedArray()
}