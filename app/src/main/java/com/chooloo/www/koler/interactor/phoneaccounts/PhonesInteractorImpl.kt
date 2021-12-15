package com.chooloo.www.koler.interactor.phoneaccounts

import android.content.Context
import com.chooloo.www.koler.contentresolver.PhoneLookupContentResolver
import com.chooloo.www.koler.contentresolver.PhonesContentResolver
import com.chooloo.www.koler.data.account.PhoneAccount
import com.chooloo.www.koler.data.account.PhoneLookupAccount
import com.chooloo.www.koler.interactor.base.BaseInteractorImpl
import io.reactivex.exceptions.OnErrorNotImplementedException

class PhonesInteractorImpl(private val context: Context) :
    BaseInteractorImpl<PhonesInteractor.Listener>(), PhonesInteractor {

    override fun lookupAccount(number: String?, callback: (PhoneLookupAccount?) -> Unit) {
        try {
            PhoneLookupContentResolver(context, number).queryItems { phones ->
                callback.invoke(phones.getOrNull(0))
            }
        } catch (e: OnErrorNotImplementedException) {
            callback.invoke(null)
        }
    }

    override fun getContactAccounts(contactId: Long, callback: (Array<PhoneAccount>?) -> Unit) {
        PhonesContentResolver(context, contactId).queryItems {
            callback.invoke(it.toTypedArray())
        }
    }
}