package com.chooloo.www.callmanager.ui.recents

import com.chooloo.www.callmanager.entity.RecentCall
import com.chooloo.www.callmanager.ui.cursor.CursorMvpView

interface RecentsMvpView : CursorMvpView {
    override val itemCount: Int
    fun openRecent(recentCall: RecentCall)
}