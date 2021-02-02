package com.chooloo.www.callmanager.ui.recents

import com.chooloo.www.callmanager.entity.Recent
import com.chooloo.www.callmanager.ui.cursor.CursorMvpPresenter

interface RecentsMvpPresenter<V : RecentsMvpView?> : CursorMvpPresenter<V> {
    fun onRecentItemClick(recent: Recent)
    fun onRecentItemLongClick(recent: Recent): Boolean
}