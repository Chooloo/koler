package com.chooloo.www.koler.ui.recent

import com.chooloo.www.koler.ui.base.MvpPresenter

interface RecentMvpPresenter<V : RecentMvpView> : MvpPresenter<V> {

    // contact actions
    fun onActionCall()
    fun onActionSms()
    fun onActionEdit()
    fun onActionInfo()
    fun onActionDelete()
}