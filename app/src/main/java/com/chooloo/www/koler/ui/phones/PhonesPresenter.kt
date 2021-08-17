package com.chooloo.www.koler.ui.phones

import PhoneAccount
import com.chooloo.www.koler.R
import com.chooloo.www.koler.ui.list.ListPresenter

class PhonesPresenter<V : PhonesContract.View>(mvpView: V) :
    ListPresenter<PhoneAccount, V>(mvpView),
    PhonesContract.Presenter<V> {

    override fun onPhonesChanged(phones: ArrayList<PhoneAccount>) {
        view.convertBundleToList(phones).let { view.updateData(it) }
    }

    override fun onPhoneItemClick(phoneAccount: PhoneAccount) {
        view.callNumber(phoneAccount.number)
    }

    override fun onPhoneLongItemClick(phoneAccount: PhoneAccount) {
        view.apply {
            clipboardText(phoneAccount.number)
            showMessage(getString(R.string.number_copied_to_clipboard))
        }
    }
}