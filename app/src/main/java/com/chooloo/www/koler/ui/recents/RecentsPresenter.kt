package com.chooloo.www.koler.ui.recents

import com.chooloo.www.koler.data.ListBundle
import com.chooloo.www.koler.data.Recent
import com.chooloo.www.koler.ui.list.ListPresenter

class RecentsPresenter<V : RecentsContract.View>(mvpView: V) :
    ListPresenter<Recent, V>(mvpView),
    RecentsContract.Presenter<V> {
    
    override fun onRecentsChanged(recents: ArrayList<Recent>) {
        view.updateData(ListBundle.fromRecents(recents))
    }

    override fun onRecentItemClick(recent: Recent) {
        view.openRecent(recent)
    }

    override fun onRecentItemLongClick(recent: Recent) {}
}