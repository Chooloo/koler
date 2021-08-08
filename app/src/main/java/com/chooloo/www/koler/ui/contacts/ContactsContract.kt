package com.chooloo.www.koler.ui.contacts

import com.chooloo.www.koler.data.Contact
import com.chooloo.www.koler.ui.list.ListContract

interface ContactsContract : ListContract {
    interface View : ListContract.View<Contact> {
        fun openContact(contact: Contact)
    }

    interface Presenter<V : View> : ListContract.Presenter<Contact, V> {
        fun onContactsChanged(contacts: ArrayList<Contact>)
        fun onContactItemClick(contact: Contact)
        fun onContactItemLongClick(contact: Contact): Boolean
    }
}