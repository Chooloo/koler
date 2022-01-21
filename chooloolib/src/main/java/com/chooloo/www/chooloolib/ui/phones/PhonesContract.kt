package com.chooloo.www.chooloolib.ui.phones

import com.chooloo.www.chooloolib.data.account.PhoneAccount
import com.chooloo.www.chooloolib.ui.list.ListContract

interface PhonesContract : ListContract {
    interface View : ListContract.View<PhoneAccount> {
        val contactId: Long?
    }

    interface Controller<V : View> : ListContract.Controller<PhoneAccount, V>
}