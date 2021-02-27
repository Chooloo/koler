package com.chooloo.www.koler.ui.contact

import android.net.Uri
import com.chooloo.www.koler.ui.base.MvpView

interface ContactMvpView : MvpView {
    var contactName: String?
    var contactNumber: String?
    var contactImage: Uri?

    fun callContact()
    fun smsContact()
    fun editContact()
    fun openContact()
    fun deleteContact()
}