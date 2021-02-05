package com.chooloo.www.callmanager.ui.page

import com.chooloo.www.callmanager.ui.base.MvpPresenter

interface PageMvpPresenter<V : PageMvpView> : MvpPresenter<V> {

    fun onDialNumberChanged(number: String)
    fun onSearchTextChanged(text: String)
    fun onScrollStateChanged(state: Int)
}