package com.chooloo.www.koler.ui.page

import com.chooloo.www.koler.ui.base.MvpPresenter

interface PageMvpPresenter<V : PageMvpView> : MvpPresenter<V> {

    fun onDialNumberChanged(number: String)
    fun onSearchTextChanged(text: String)
    fun onScrollStateChanged(state: Int)
}