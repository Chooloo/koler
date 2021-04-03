package com.chooloo.www.koler.ui.contacts

import com.chooloo.www.koler.data.Contact
import com.chooloo.www.koler.data.ContactsBundle
import com.chooloo.www.koler.ui.list.ListPresenter

class ContactsPresenter<V : ContactsContract.View> : ListPresenter<V>(),
    ContactsContract.Presenter<V> {
    override fun onContactsChanged(contactsBundle: ContactsBundle) {
        mvpView?.updateContacts(contactsBundle)
    }

    override fun onContactItemClick(contact: Contact) {
        mvpView?.openContact(contact)
    }

    override fun onContactItemLongClick(contact: Contact) = true
}