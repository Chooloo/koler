package com.chooloo.www.callmanager.ui.contacts

import com.chooloo.www.callmanager.entity.Contact
import com.chooloo.www.callmanager.ui.list.ListMvpView

interface ContactsMvpView : ListMvpView {

    fun observe(): Any?
    fun openContact(contact: Contact)
}