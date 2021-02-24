package com.chooloo.www.koler.ui.main

import com.chooloo.www.koler.ui.base.MvpView

interface MainMvpView : MvpView {
    var dialpadNumber: String

    fun openMenu()
    fun openDialpad()
    fun goToSettings()
    fun goToAbout()
    fun updateSearchViewModelText(text: String?)
    fun checkIntent()
}