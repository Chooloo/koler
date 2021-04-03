package com.chooloo.www.koler.ui.contacts

import com.chooloo.www.koler.data.Contact
import com.chooloo.www.koler.data.ContactsBundle
import com.chooloo.www.koler.ui.list.ListContract

interface ContactsContract : ListContract {
    interface View : ListContract.View {
        fun openContact(contact: Contact)
        fun updateContacts(contactsBundle: ContactsBundle)
        fun setContactsFilter(filter: String?)
    }

    interface Presenter<V : View> : ListContract.Presenter<V> {
        fun onContactsChanged(contactsBundle: ContactsBundle)
        fun onContactItemClick(contact: Contact)
        fun onContactItemLongClick(contact: Contact): Boolean
    }
}