package com.chooloo.www.callmanager.ui.search

import com.chooloo.www.callmanager.ui.base.MvpPresenter

interface SearchMvpPresenter<V : SearchMvpView> : MvpPresenter<V> {

    fun onTextChanged(text: String)
    fun onFocusChanged(hasFocus: Boolean)
}