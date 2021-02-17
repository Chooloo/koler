package com.chooloo.www.koler.ui.recent

import com.chooloo.www.koler.ui.base.MvpView

interface RecentMvpView : MvpView {

    // contact actions
    fun call()
    fun sms()
    fun edit()
    fun open()
    fun delete()

    fun animateLayout()
}