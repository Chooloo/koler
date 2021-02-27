package com.chooloo.www.koler.ui.recents

import com.chooloo.www.koler.data.Recent
import com.chooloo.www.koler.ui.list.ListPresenter

class RecentsPresenter<V : RecentsMvpView> : ListPresenter<V>(), RecentsMvpPresenter<V> {
    override fun onRecentItemClick(recent: Recent) {
        mvpView?.openRecent(recent)
    }

    override fun onRecentItemLongClick(recent: Recent) {}
    
    override fun onPermissionsBlocked() {
        mvpView?.showPermissionsPage(true)
    }
}