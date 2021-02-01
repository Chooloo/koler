package com.chooloo.www.callmanager.ui.contacts

import com.chooloo.www.callmanager.entity.Contact
import com.chooloo.www.callmanager.ui.cursor.CursorMvpView

interface ContactsMvpView : CursorMvpView {
    fun openContact(contact: Contact)
}