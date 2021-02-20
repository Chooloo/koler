package com.chooloo.www.koler.ui.main

import com.chooloo.www.koler.ui.base.MvpView

interface MainMvpView : MvpView {
    var dialpadNumber: String
    var isDialpadVisible: Boolean
    var isMenuVisible: Boolean

    fun goToSettings()
    fun goToAbout()
    fun updateSearchViewModelText(text: String?)
    fun checkIntent()
}