package com.chooloo.www.koler.ui.contacts

import com.chooloo.www.koler.data.Contact
import com.chooloo.www.koler.data.ListBundle
import com.chooloo.www.koler.ui.list.ListPresenter

class ContactsPresenter<V : ContactsContract.View>(mvpView: V) :
    ListPresenter<Contact, V>(mvpView),
    ContactsContract.Presenter<V> {
    
    override fun onContactsChanged(contacts: ArrayList<Contact>) {
        mvpView.updateData(ListBundle.fromContacts(contacts, withFavs = true))
    }

    override fun onContactItemClick(contact: Contact) {
        mvpView.openContact(contact)
    }

    override fun onContactItemLongClick(contact: Contact) = true
}