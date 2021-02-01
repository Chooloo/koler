package com.chooloo.www.callmanager.ui.recents

import com.chooloo.www.callmanager.entity.RecentCall
import com.chooloo.www.callmanager.ui.cursor.CursorPresenter

class RecentsPresenter<V : RecentsMvpView?> : CursorPresenter<V>(), RecentsMvpPresenter<V> {
    override fun onRecentItemClick(recentCall: RecentCall) {}
    override fun onRecentItemLongClick(recentCall: RecentCall): Boolean {
        return true
    }
}