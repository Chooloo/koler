package com.chooloo.www.callmanager.ui.recents

import com.chooloo.www.callmanager.entity.Recent
import com.chooloo.www.callmanager.ui.list.ListMvpView

interface RecentsMvpView : ListMvpView {
    override val itemCount: Int
    fun openRecent(recent: Recent)
}