package com.chooloo.www.koler.ui.search

import com.chooloo.www.koler.ui.base.MvpPresenter

interface SearchMvpPresenter<V : SearchMvpView> : MvpPresenter<V> {

    fun onTextChanged(text: String)
    fun onFocusChanged(hasFocus: Boolean)
}