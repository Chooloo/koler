package com.chooloo.www.koler.ui.contact

import com.chooloo.www.koler.ui.base.MvpPresenter

interface ContactMvpPresenter<V : ContactMvpView> : MvpPresenter<V> {

    // contact actions
    fun onActionCall()
    fun onActionSms()
    fun onActionEdit()
    fun onActionInfo()
    fun onActionDelete()
}