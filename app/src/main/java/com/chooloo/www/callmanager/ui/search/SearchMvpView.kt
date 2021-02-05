package com.chooloo.www.callmanager.ui.search

import com.chooloo.www.callmanager.ui.base.MvpView

interface SearchMvpView : MvpView {
    val text: String

    fun setFocus()
    fun showIcon(isShow: Boolean)
}