package com.chooloo.www.koler.ui.contacts

import com.chooloo.www.koler.data.Contact
import com.chooloo.www.koler.data.ContactsBundle
import com.chooloo.www.koler.ui.base.MvpPresenter

interface ContactsMvpPresenter<V : ContactsMvpView> : MvpPresenter<V> {
    fun onContactsChanged(contactsBundle: ContactsBundle)
    fun onSearchTextChanged(text: String?)
    fun onDialpadNumberChanged(number: String?)
    fun onContactItemClick(contact: Contact)
    fun onContactItemLongClick(contact: Contact): Boolean
    fun onPermissionsBlocked(permissions: Array<String>)
}