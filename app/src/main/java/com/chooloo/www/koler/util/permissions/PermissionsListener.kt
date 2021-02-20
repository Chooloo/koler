package com.chooloo.www.koler.util.permissions

abstract class PermissionsListener {
    fun onDenied(deniedPermissions: Array<String>) {
    }

    fun onBlocked(blockedPermissions: Array<String>): Boolean {
        return false
    }

    fun onJustBlocked(justBlockedPermissions: Array<String>) {
    }

    abstract fun onGranted()
}

