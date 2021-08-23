package com.chooloo.www.koler.ui.recentpreferences

import com.chooloo.www.koler.ui.base.BasePresenter

class RecentPreferencesPresenter<V : RecentPreferencesContract.View>(view: V) :
    BasePresenter<V>(view),
    RecentPreferencesContract.Presenter<V> {

    override fun onBlockNumberClick() {
        view.toggleNumberBlocked(true)
    }

    override fun onUnblockNumberClick() {
        view.toggleNumberBlocked(false)
    }
}