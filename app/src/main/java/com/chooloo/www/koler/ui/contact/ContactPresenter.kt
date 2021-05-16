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
            isStarIconVisible = contact.starred
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

    override fun onActionMenu() {
        mvpView?.showMenu()
    }

    override fun onActionFav() {
        mvpView?.apply {
            setFavorite(!_contact.starred)
            isStarIconVisible = _contact.starred
        }
    }
}