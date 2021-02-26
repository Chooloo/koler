package com.chooloo.www.koler.ui.contacts

import com.chooloo.www.koler.entity.Contact
import com.chooloo.www.koler.ui.list.ListPresenter

class ContactsPresenter<V : ContactsMvpView> : ListPresenter<V>(), ContactsMvpPresenter<V> {
    override fun onContactItemClick(contact: Contact) {
        mvpView?.openContact(contact)
    }

    override fun onContactItemLongClick(contact: Contact) = true

    override fun onPermissionsBlocked() {
        mvpView?.showPermissionsPage(true)
    }
}