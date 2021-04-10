package com.chooloo.www.koler.ui.recents

import com.chooloo.www.koler.data.Recent
import com.chooloo.www.koler.data.RecentsBundle
import com.chooloo.www.koler.ui.list.ListContract

interface RecentsContract : ListContract {
    interface View : ListContract.View<Recent> {
        fun openRecent(recent: Recent)
    }

    interface Presenter<V : View> : ListContract.Presenter<Recent, V> {
        fun onRecentsChanged(recentsBundle: RecentsBundle)
        fun onRecentItemClick(recent: Recent)
        fun onRecentItemLongClick(recent: Recent)
    }
}