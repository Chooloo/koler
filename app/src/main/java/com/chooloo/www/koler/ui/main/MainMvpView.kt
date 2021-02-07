package com.chooloo.www.koler.ui.main

import com.chooloo.www.koler.ui.base.MvpView

interface MainMvpView : MvpView {
    var dialpadNumber: String
    val isDialpadShown: Boolean

    fun showDialpad(isShow: Boolean)
    fun showMenu(isShow: Boolean)
    fun goToSettings()
    fun goToAbout()
}