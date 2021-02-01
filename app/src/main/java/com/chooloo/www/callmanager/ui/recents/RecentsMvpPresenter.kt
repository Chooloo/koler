package com.chooloo.www.callmanager.ui.recents

import com.chooloo.www.callmanager.entity.RecentCall
import com.chooloo.www.callmanager.ui.cursor.CursorMvpPresenter

interface RecentsMvpPresenter<V : RecentsMvpView?> : CursorMvpPresenter<V> {
    fun onRecentItemClick(recentCall: RecentCall)
    fun onRecentItemLongClick(recentCall: RecentCall): Boolean
}