package com.chooloo.www.callmanager.ui.about

import com.chooloo.www.callmanager.ui.base.MvpView

interface AboutMvpView : MvpView {

    fun openSource()
    fun sendEmail()
    fun reportBug()
    fun rateApp()
    fun donate()
}