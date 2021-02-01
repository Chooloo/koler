package com.chooloo.www.callmanager.ui.cursor

import com.chooloo.www.callmanager.ui.base.MvpPresenter

interface CursorMvpPresenter<V : CursorMvpView?> : MvpPresenter<V> {
    fun onNoPermissions()
    fun onResults()
    fun onNoResults()
    fun onEnablePermissionClick()
}