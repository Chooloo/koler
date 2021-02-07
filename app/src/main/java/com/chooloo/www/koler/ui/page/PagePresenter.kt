package com.chooloo.www.koler.ui.page

import androidx.recyclerview.widget.RecyclerView
import com.chooloo.www.koler.ui.base.BasePresenter

class PagePresenter<V : PageMvpView> : BasePresenter<V>(), PageMvpPresenter<V> {
    override fun onDialNumberChanged(number: String) {
        // TODO "implement"
    }

    override fun onSearchTextChanged(text: String) {
        // TODO implement
    }

    override fun onScrollStateChanged(state: Int) {
        if (state == RecyclerView.SCROLL_STATE_DRAGGING or RecyclerView.SCROLL_STATE_SETTLING) {
            mvpView?.setSearchBarFocused(false)
        }
    }
}