package com.chooloo.www.koler.ui.about

import com.chooloo.www.koler.ui.base.MvpView

interface AboutMvpView : MvpView {
    fun openSource()
    fun sendEmail()
    fun reportBug()
    fun rateApp()
    fun donate()
}