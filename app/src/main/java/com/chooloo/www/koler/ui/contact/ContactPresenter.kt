package com.chooloo.www.koler.ui.contact

import android.net.Uri
import com.chooloo.www.koler.data.Contact
import com.chooloo.www.koler.ui.base.BasePresenter

class ContactPresenter<V : ContactContract.View> : BasePresenter<V>(),
    ContactContract.Presenter<V> {
    private lateinit var _contact: Contact

    override fun onLoadContact(contact: Contact) {
        _contact = contact
        mvpView?.apply {
            contactName = contact.name
//            contact.number?.let { contactNumber = contact.number }
            contact.photoUri?.let { contactImage = Uri.parse(it) }
            isStarIconActivated = contact.starred
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

    override fun onActionFav() {
        mvpView?.setFavorite(!_contact.starred)
//        _contact.starred = !_contact.starred
        mvpView?.isStarIconActivated = _contact.starred
    }
}