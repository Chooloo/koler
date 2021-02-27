package com.chooloo.www.koler.ui.recents

import com.chooloo.www.koler.data.Recent
import com.chooloo.www.koler.data.RecentsBundle
import com.chooloo.www.koler.ui.list.ListMvpPresenter

interface RecentsMvpPresenter<V : RecentsMvpView> : ListMvpPresenter<V> {
    fun onRecentsChanged(recentsBundle: RecentsBundle)
    fun onSearchTextChanged(text: String?)
    fun onDialpadNumberChanged(number: String?)
    fun onRecentItemClick(recent: Recent)
    fun onRecentItemLongClick(recent: Recent)
    fun onPermissionsBlocked(permissions: Array<String>)
}