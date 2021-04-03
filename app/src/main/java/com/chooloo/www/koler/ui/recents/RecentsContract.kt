package com.chooloo.www.koler.ui.recents

import com.chooloo.www.koler.data.Recent
import com.chooloo.www.koler.data.RecentsBundle
import com.chooloo.www.koler.ui.list.ListContract

interface RecentsContract : ListContract {
    interface View : ListContract.View {
        fun openRecent(recent: Recent)
        fun updateRecents(recentsBundle: RecentsBundle)
        fun setRecentsFilter(filter: String?)
    }

    interface Presenter<V : View> : ListContract.Presenter<V> {
        fun onRecentsChanged(recentsBundle: RecentsBundle)
        fun onRecentItemClick(recent: Recent)
        fun onRecentItemLongClick(recent: Recent)
    }
}