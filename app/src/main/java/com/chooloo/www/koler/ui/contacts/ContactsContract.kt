package com.chooloo.www.koler.ui.contacts

import com.chooloo.www.koler.data.account.ContactAccount
import com.chooloo.www.koler.ui.list.ListContract

interface ContactsContract : ListContract {
    interface View : ListContract.View<ContactAccount> {
        fun openContact(contact:ContactAccount)
    }

    interface Controller<V : View> : ListContract.Controller<ContactAccount, V> {
    }
}