package com.chooloo.www.koler.ui.recents

import com.chooloo.www.koler.data.Recent
import com.chooloo.www.koler.data.RecentsBundle
import com.chooloo.www.koler.ui.list.ListPresenter

class RecentsPresenter<V : RecentsContract.View> : ListPresenter<Recent, V>(),
    RecentsContract.Presenter<V> {
    override fun onRecentsChanged(recentsBundle: RecentsBundle) {
        mvpView?.updateData(recentsBundle.listBundleByDates)
    }

    override fun onRecentItemClick(recent: Recent) {
        mvpView?.openRecent(recent)
    }

    override fun onRecentItemLongClick(recent: Recent) {}
}