package com.chooloo.www.koler.ui.recents

import com.chooloo.www.koler.data.Recent
import com.chooloo.www.koler.data.RecentsBundle
import com.chooloo.www.koler.ui.list.ListPresenter

class RecentsPresenter<V : RecentsContract.View> : ListPresenter<V>(),
    RecentsContract.Presenter<V> {
    override fun onRecentsChanged(recentsBundle: RecentsBundle) {
        mvpView?.updateRecents(recentsBundle)
    }

    override fun onSearchTextChanged(text: String?) {
        mvpView?.setRecentsFilter(text)
    }

    override fun onDialpadNumberChanged(number: String?) {
        mvpView?.setRecentsFilter(number)
    }

    override fun onRecentItemClick(recent: Recent) {
        mvpView?.openRecent(recent)
    }

    override fun onRecentItemLongClick(recent: Recent) {}

    override fun onPermissionsBlocked(permissions: Array<String>) {
        mvpView?.showPermissionsPage(true)
    }
}