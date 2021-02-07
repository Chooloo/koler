package com.chooloo.www.koler.ui.list

import android.Manifest.permission
import com.chooloo.www.koler.ui.base.BasePresenter
import com.chooloo.www.koler.util.RC_READ_CONTACTS

open class ListPresenter<V : ListMvpView> : BasePresenter<V>(), ListMvpPresenter<V> {
    override fun onNoPermissions() {
        onNoResults()
    }

    override fun onResults() {
        mvpView?.showEmptyPage(false)
    }

    override fun onNoResults() {
        mvpView?.showEmptyPage(true)
    }

    override fun onEnablePermissionClick() {
        mvpView?.askForPermission(permission.READ_CONTACTS, RC_READ_CONTACTS)
    }
}