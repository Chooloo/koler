package com.chooloo.www.callmanager.ui.cursor

import android.Manifest.permission
import com.chooloo.www.callmanager.ui.base.BasePresenter
import com.chooloo.www.callmanager.util.PermissionUtils.RC_READ_CONTACTS

open class CursorPresenter<V : CursorMvpView?> : BasePresenter<V>(), CursorMvpPresenter<V> {
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