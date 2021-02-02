package com.chooloo.www.callmanager.ui.list

import com.chooloo.www.callmanager.ui.base.MvpPresenter

interface ListMvpPresenter<V : ListMvpView?> : MvpPresenter<V> {
    fun onNoPermissions()
    fun onResults()
    fun onNoResults()
    fun onEnablePermissionClick()
}