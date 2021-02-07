package com.chooloo.www.koler.ui.search

import com.chooloo.www.koler.ui.base.MvpView

interface SearchMvpView : MvpView {
    val text: String

    fun setFocus()
    fun showIcon(isShow: Boolean)
}