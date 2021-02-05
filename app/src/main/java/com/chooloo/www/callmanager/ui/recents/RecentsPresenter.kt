package com.chooloo.www.callmanager.ui.recents

import com.chooloo.www.callmanager.entity.Recent
import com.chooloo.www.callmanager.ui.list.ListPresenter

class RecentsPresenter<V : RecentsMvpView> : ListPresenter<V>(), RecentsMvpPresenter<V> {
    override fun onRecentItemClick(recent: Recent) {}
    override fun onRecentItemLongClick(recent: Recent) {}
}