package com.chooloo.www.koler.ui.phones

import PhoneAccount
import com.chooloo.www.koler.data.PhonesBundle
import com.chooloo.www.koler.ui.list.ListPresenter

class PhonesPresenter<V : PhonesContract.View> : ListPresenter<V>(), PhonesContract.Presenter<V> {
    override fun onPhonesChanged(phonesBundle: PhonesBundle) {
        mvpView?.updatePhoneAccounts(phonesBundle)
    }

    override fun onPhoneItemClick(phoneAccount: PhoneAccount) {
        mvpView?.callNumber(phoneAccount.number)
    }
}