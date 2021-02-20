package com.chooloo.www.koler.ui.contact

import android.net.Uri
import com.chooloo.www.koler.entity.Contact
import com.chooloo.www.koler.ui.base.BasePresenter

class ContactPresenter<V : ContactMvpView> : BasePresenter<V>(), ContactMvpPresenter<V> {

    override fun onLoadContact(contact: Contact) {
        mvpView?.apply {
            contactName = contact.name
            contact.number?.let { contactNumber = contact.number }
            contact.photoUri?.let { contactImage = Uri.parse(it) }
        }
    }

    override fun onActionCall() {
        mvpView?.callContact()
    }

    override fun onActionSms() {
        mvpView?.smsContact()
    }

    override fun onActionEdit() {
        mvpView?.editContact()
    }

    override fun onActionInfo() {
        mvpView?.openContact()
    }

    override fun onActionDelete() {
        mvpView?.deleteContact()
    }
}