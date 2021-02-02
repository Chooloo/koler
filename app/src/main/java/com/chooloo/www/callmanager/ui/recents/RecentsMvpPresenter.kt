package com.chooloo.www.callmanager.ui.recents

import com.chooloo.www.callmanager.entity.Recent
import com.chooloo.www.callmanager.ui.list.ListMvpPresenter

interface RecentsMvpPresenter<V : RecentsMvpView?> : ListMvpPresenter<V> {
    fun onRecentItemClick(recent: Recent)
    fun onRecentItemLongClick(recent: Recent): Boolean
}