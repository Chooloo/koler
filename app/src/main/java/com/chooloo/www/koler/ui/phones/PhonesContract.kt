package com.chooloo.www.koler.ui.phones

import PhoneAccount
import com.chooloo.www.koler.data.ListBundle
import com.chooloo.www.koler.ui.list.ListContract

interface PhonesContract : ListContract {
    interface View : ListContract.View<PhoneAccount> {
        fun callNumber(number: String)
        fun clipboardText(text: String)
        fun convertBundleToList(phones: ArrayList<PhoneAccount>): ListBundle<PhoneAccount>
    }

    interface Presenter<V : View> : ListContract.Presenter<PhoneAccount, V> {
        fun onPhonesChanged(phones: ArrayList<PhoneAccount>)
        fun onPhoneItemClick(phoneAccount: PhoneAccount)
        fun onPhoneLongItemClick(phoneAccount: PhoneAccount)
    }
}