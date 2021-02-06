package com.chooloo.www.callmanager.ui.contacts

import com.chooloo.www.callmanager.entity.Contact
import com.chooloo.www.callmanager.ui.base.MvpPresenter
import com.karumi.dexter.listener.PermissionGrantedResponse

interface ContactsMvpPresenter<V : ContactsMvpView> : MvpPresenter<V> {
    fun onContactItemClick(contact: Contact)
    fun onContactItemLongClick(contact: Contact): Boolean
}