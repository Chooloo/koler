package com.chooloo.www.koler.ui.list

import com.chooloo.www.koler.ui.base.BasePresenter

open class ListPresenter<V : ListContract.View> : BasePresenter<V>(), ListContract.Presenter<V> {
    override fun onResults() {
        mvpView?.showEmptyPage(false)
    }

    override fun onNoResults() {
        mvpView?.showEmptyPage(true)
    }

    override fun onEnablePermissionClick() {
//        mvpView?.askForPermission(permission.READ_CONTACTS, RC_READ_CONTACTS)
    }
}