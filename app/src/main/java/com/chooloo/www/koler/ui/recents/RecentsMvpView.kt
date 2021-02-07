package com.chooloo.www.koler.ui.recents

import com.chooloo.www.koler.entity.Recent
import com.chooloo.www.koler.ui.list.ListMvpView

interface RecentsMvpView : ListMvpView {
    override val itemCount: Int

    fun observe(): Any?
    fun openRecent(recent: Recent)
}