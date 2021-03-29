package com.chooloo.www.koler.ui.phones

import com.chooloo.www.koler.data.PhonesBundle
import com.chooloo.www.koler.ui.list.ListContract

interface PhonesContract : ListContract {
    interface View : ListContract.View {
        fun updatePhoneAccounts(phonesBundle: PhonesBundle)
    }

    interface Presenter<V : View> : ListContract.Presenter<V> {
        fun onPhonesChanged(phonesBundle: PhonesBundle)
    }
}