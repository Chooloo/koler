package com.chooloo.www.koler.ui.recents

import com.chooloo.www.koler.data.Recent
import com.chooloo.www.koler.data.RecentsBundle
import com.chooloo.www.koler.ui.list.ListMvpView

interface RecentsMvpView : ListMvpView {
    fun observe(): Any?
    fun openRecent(recent: Recent)
    fun updateRecents(recentsBundle: RecentsBundle)
    fun setRecentsFilter(filter: String?)
}