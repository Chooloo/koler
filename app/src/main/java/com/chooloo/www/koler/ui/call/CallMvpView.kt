package com.chooloo.www.koler.ui.call

import android.net.Uri
import com.chooloo.www.koler.data.Contact
import com.chooloo.www.koler.ui.base.MvpView

interface CallMvpView : MvpView {
    var stateText: String?
    var callerNameText: String?
    var callerImageURI: Uri?

    fun getContact(number: String): Contact
    fun switchToActiveCallUI()
    fun setStateRed()
}