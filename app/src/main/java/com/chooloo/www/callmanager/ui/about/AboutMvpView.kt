package com.chooloo.www.callmanager.ui.about

import com.chooloo.www.callmanager.ui.base.MvpView

interface AboutMvpView : MvpView {
    override fun onSetup()
    fun openChangelog()
    fun openSource()
    fun sendEmail()
    fun reportBug()
    fun rateApp()
    fun donate()
}