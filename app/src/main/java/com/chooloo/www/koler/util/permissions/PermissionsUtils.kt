package com.chooloo.www.koler.util.permissions

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.telecom.TelecomManager
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.chooloo.www.koler.util.permissions.PermissionsActivity

const val RC_DEFAULT_DIALER = 0
const val RC_READ_CONTACTS = 1
const val RC_DEFAULT = 2

// default dialer

fun Context.isDefaultDialer(): Boolean {
    return this.getSystemService(TelecomManager::class.java)?.defaultDialerPackage == this.applicationContext?.packageName
}

fun Activity.requestDefaultDialer() {
    startActivityForResult(Intent(TelecomManager.ACTION_CHANGE_DEFAULT_DIALER).also {
        it.putExtra(TelecomManager.EXTRA_CHANGE_DEFAULT_DIALER_PACKAGE_NAME, packageName)
    }, RC_DEFAULT_DIALER)
}

// general permissions

fun Activity.hasSelfPermission(permission: String): Boolean {
    return checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
}

fun Activity.hasSelfPermissions(permissions: Array<String>): Boolean {
    return permissions.all { hasSelfPermission(it) }
}

fun Activity.requestPermissions(permissions: Array<String>) {
    ActivityCompat.requestPermissions(this, permissions, 0)
}

fun Activity.runWithPermissions(
    permissions: Array<String>,
    callback: () -> Unit,
    rationaleMessage: String? = null
) {
    checkPermissions(permissions, rationaleMessage, object : PermissionsListener() {
        override fun onGranted() {
            callback()
        }
    })
}

fun Fragment.runWithPermissions(permissions: Array<String>, callback: () -> Unit): Any? {
    return activity?.runWithPermissions(permissions, callback)
}

fun Activity.checkPermissions(
    permissions: Array<String>,
    rationaleMessage: String? = null,
    permissionsListener: PermissionsListener? = null,
) {
//    if (permissions.all { checkSelfPermission(it) != PackageManager.PERMISSION_GRANTED }) {
//        permissionsListener?.onGrantedCallback?.invoke()
//    } else {
    PermissionsActivity.listener = permissionsListener
    Intent(this, PermissionsActivity::class.java).also {
        it.putExtra(PermissionsActivity.EXTRA_PERMISSIONS, permissions)
        it.putExtra(PermissionsActivity.EXTRA_RATIONAL_MESSAGE, rationaleMessage)
        startActivity(it)
    }
//    }

}

