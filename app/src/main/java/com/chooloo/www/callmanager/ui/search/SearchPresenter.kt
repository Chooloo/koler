package com.chooloo.www.callmanager.ui.search

import com.chooloo.www.callmanager.ui.base.BasePresenter

class SearchPresenter<V : SearchMvpView> : BasePresenter<V>(), SearchMvpPresenter<V> {
    override fun onTextChanged(text: String) {}
    override fun onFocusChanged(hasFocus: Boolean) {
        mvpView?.showIcon(!hasFocus)
    }
}