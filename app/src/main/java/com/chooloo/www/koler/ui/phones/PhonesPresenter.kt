package com.chooloo.www.koler.ui.phones

import PhoneAccount
import com.chooloo.www.koler.R
import com.chooloo.www.koler.data.PhonesBundle
import com.chooloo.www.koler.ui.list.ListPresenter

class PhonesPresenter<V : PhonesContract.View> : ListPresenter<PhoneAccount, V>(),
    PhonesContract.Presenter<V> {
    override fun onPhonesChanged(phonesBundle: PhonesBundle) {
        mvpView?.convertBundleToList(phonesBundle)?.let { mvpView?.updateData(it) }
    }

    override fun onPhoneItemClick(phoneAccount: PhoneAccount) {
        mvpView?.callNumber(phoneAccount.number)
    }

    override fun onPhoneLongItemClick(phoneAccount: PhoneAccount) {
        mvpView?.apply {
            clipboardText(phoneAccount.number)
            showMessage(getString(R.string.number_copied_to_clipboard))
        }
    }
}