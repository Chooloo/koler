package com.chooloo.www.koler.ui.page

import com.chooloo.www.koler.ui.base.MvpView

interface PageMvpView : MvpView {

    fun setSearchBarFocused(isFocused: Boolean)
}