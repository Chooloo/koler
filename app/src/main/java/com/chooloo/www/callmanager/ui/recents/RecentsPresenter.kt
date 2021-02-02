package com.chooloo.www.callmanager.ui.recents

import com.chooloo.www.callmanager.entity.Recent
import com.chooloo.www.callmanager.ui.cursor.CursorPresenter

class RecentsPresenter<V : RecentsMvpView?> : CursorPresenter<V>(), RecentsMvpPresenter<V> {
    override fun onRecentItemClick(recent: Recent) {}
    override fun onRecentItemLongClick(recent: Recent): Boolean {
        return true
    }
}