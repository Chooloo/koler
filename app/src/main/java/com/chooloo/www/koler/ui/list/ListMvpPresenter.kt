package com.chooloo.www.koler.ui.list

import com.chooloo.www.koler.ui.base.MvpPresenter

interface ListMvpPresenter<V : ListMvpView> : MvpPresenter<V> {
    fun onNoPermissions()
    fun onResults()
    fun onNoResults()
    fun onEnablePermissionClick()
}