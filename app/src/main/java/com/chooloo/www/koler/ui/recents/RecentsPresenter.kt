package com.chooloo.www.koler.ui.recents

import com.chooloo.www.koler.entity.Recent
import com.chooloo.www.koler.ui.list.ListPresenter

class RecentsPresenter<V : RecentsMvpView> : ListPresenter<V>(), RecentsMvpPresenter<V> {

    override fun onRecentItemClick(recent: Recent) {}
    override fun onRecentItemLongClick(recent: Recent) {}
}