package com.chooloo.www.koler.ui.about

interface AboutMvpView : MvpView {
    fun openSource()
    fun sendEmail()
    fun reportBug()
    fun rateApp()
    fun donate()
}