package com.chooloo.www.koler.ui.phones

import PhoneAccount
import com.chooloo.www.koler.data.ListBundle
import com.chooloo.www.koler.data.PhonesBundle
import com.chooloo.www.koler.ui.list.ListContract

interface PhonesContract : ListContract {
    interface View : ListContract.View<PhoneAccount> {
        fun callNumber(number: String)
        fun clipboardText(text: String)
        fun convertBundleToList(phonesBundle: PhonesBundle): ListBundle<PhoneAccount>
    }

    interface Presenter<V : View> : ListContract.Presenter<PhoneAccount, V> {
        fun onPhonesChanged(phonesBundle: PhonesBundle)
        fun onPhoneItemClick(phoneAccount: PhoneAccount)
        fun onPhoneLongItemClick(phoneAccount: PhoneAccount)
    }
}