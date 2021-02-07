package com.chooloo.www.koler.ui.search

import com.chooloo.www.koler.ui.base.BasePresenter

class SearchPresenter<V : SearchMvpView> : BasePresenter<V>(), SearchMvpPresenter<V> {
    override fun onTextChanged(text: String) {}
    override fun onFocusChanged(hasFocus: Boolean) {
        mvpView?.showIcon(!hasFocus)
    }
}