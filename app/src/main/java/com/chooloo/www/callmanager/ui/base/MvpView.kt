package com.chooloo.www.callmanager.ui.base

import androidx.annotation.StringRes

interface MvpView {
    fun onGetPermissions(): Array<String>
    fun onSetup()

    fun hasPermissions(): Boolean
    fun hasPermission(permission: String): Boolean

    fun askForPermission(permission: String, requestCode: Int)
    fun askForPermissions(permissions: Array<String>, requestCode: Int)
    fun askForPermissions()

    fun showMessage(message: String)
    fun showMessage(@StringRes stringResId: Int)
    fun showError(message: String)
    fun showError(@StringRes stringResId: Int)
}