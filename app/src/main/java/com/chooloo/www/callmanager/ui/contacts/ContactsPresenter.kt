package com.chooloo.www.callmanager.ui.contacts

import com.chooloo.www.callmanager.entity.Contact
import com.chooloo.www.callmanager.livedata.ContactsLiveData
import com.chooloo.www.callmanager.ui.list.ListPresenter
import com.karumi.dexter.listener.PermissionGrantedResponse

class ContactsPresenter<V : ContactsMvpView> : ListPresenter<V>(), ContactsMvpPresenter<V> {

    override fun onContactItemClick(contact: Contact) {
        mvpView?.openContact(contact)
    }

    override fun onContactItemLongClick(contact: Contact): Boolean {
        return true
    }
}