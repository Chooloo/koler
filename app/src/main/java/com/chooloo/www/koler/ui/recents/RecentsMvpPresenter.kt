package com.chooloo.www.koler.ui.recents

import com.chooloo.www.koler.entity.Recent
import com.chooloo.www.koler.ui.list.ListMvpPresenter

interface RecentsMvpPresenter<V : RecentsMvpView> : ListMvpPresenter<V> {
    fun onRecentItemClick(recent: Recent)
    fun onRecentItemLongClick(recent: Recent)
    fun onPermissionsBlocked()
}