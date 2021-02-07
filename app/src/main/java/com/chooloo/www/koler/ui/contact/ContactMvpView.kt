package com.chooloo.www.koler.ui.contact

import com.chooloo.www.koler.ui.base.MvpView

interface ContactMvpView : MvpView {

    // contact actions
    fun call()
    fun sms()
    fun edit()
    fun open()
    fun delete()

    fun animateLayout()
}