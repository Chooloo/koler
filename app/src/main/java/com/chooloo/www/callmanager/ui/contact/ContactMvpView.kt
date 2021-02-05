package com.chooloo.www.callmanager.ui.contact

import com.chooloo.www.callmanager.ui.base.MvpView

interface ContactMvpView : MvpView {

    // contact actions
    fun call()
    fun sms()
    fun edit()
    fun open()
    fun delete()

    fun animateLayout()
}