package com.chooloo.www.callmanager.ui.contact

import com.chooloo.www.callmanager.ui.base.MvpPresenter

interface ContactMvpPresenter<V : ContactMvpView> : MvpPresenter<V> {

    // contact actions
    fun onActionCall()
    fun onActionSms()
    fun onActionEdit()
    fun onActionInfo()
    fun onActionDelete()
}