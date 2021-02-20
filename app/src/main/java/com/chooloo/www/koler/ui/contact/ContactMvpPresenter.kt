package com.chooloo.www.koler.ui.contact

import com.chooloo.www.koler.entity.Contact
import com.chooloo.www.koler.ui.base.MvpPresenter

interface ContactMvpPresenter<V : ContactMvpView> : MvpPresenter<V> {
    fun onLoadContact(contact: Contact)
    fun onActionCall()
    fun onActionSms()
    fun onActionEdit()
    fun onActionInfo()
    fun onActionDelete()
}