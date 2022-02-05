package com.chooloo.www.chooloolib.ui.contacts

import com.chooloo.www.chooloolib.adapter.ContactsAdapter
import com.chooloo.www.chooloolib.data.account.ContactAccount
import com.chooloo.www.chooloolib.ui.list.ListContract

interface ContactsContract : ListContract {
    interface View : ListContract.View<ContactAccount> {
    }

    interface Controller : ListContract.Controller<ContactAccount, View> {
    }
}