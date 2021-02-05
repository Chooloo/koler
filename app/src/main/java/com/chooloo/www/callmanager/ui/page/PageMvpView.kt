package com.chooloo.www.callmanager.ui.page

import com.chooloo.www.callmanager.ui.base.MvpView

interface PageMvpView : MvpView {

    fun setSearchBarFocused(isFocused: Boolean)
}