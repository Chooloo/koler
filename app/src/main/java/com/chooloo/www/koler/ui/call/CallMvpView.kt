package com.chooloo.www.koler.ui.call

import android.net.Uri
import com.chooloo.www.koler.entity.Contact
import com.chooloo.www.koler.ui.base.MvpView

interface CallMvpView : MvpView {
    var status: String?
    var callerName: String?
    var callerImage:Uri?

    fun answer()
    fun reject()
    fun setAudioInCall()
    fun openCallActions()
    fun lookupContact(number: String): Contact
}