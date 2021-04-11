package com.chooloo.www.koler.util.permissions

import android.app.Activity
import android.content.Context
import android.content.Context.TELECOM_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.telecom.TelecomManager
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment

const val RC_DEFAULT_DIALER = 0
const val RC_READ_CONTACTS = 1
const val RC_DEFAULT = 2

//region default dialer
fun Activity.checkDefaultDialer() {
    if (!isDefaultDialer()) {
        requestDefaultDialer()
    }
}

fun Context.isDefaultDialer() =
    packageName == (getSystemService(TELECOM_SERVICE) as TelecomManager).defaultDialerPackage

fun Activity.requestDefaultDialer() {
    val intent = Intent(TelecomManager.ACTION_CHANGE_DEFAULT_DIALER)
        .putExtra(TelecomManager.EXTRA_CHANGE_DEFAULT_DIALER_PACKAGE_NAME, packageName)
    startActivityForResult(intent, RC_DEFAULT_DIALER)
}
//endregion

//region general permissions
fun Activity.hasSelfPermission(permission: String) =
    checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED

fun Activity.hasSelfPermissions(permissions: Array<String>) =
    permissions.all { hasSelfPermission(it) }

fun Activity.requestPermissions(permissions: Array<String>) {
    ActivityCompat.requestPermissions(this, permissions, 0)
}

fun Fragment.runWithPermissions(
    permissions: Array<String>,
    grantedCallback: () -> Unit,
    deniedCallback: (() -> Unit)? = null,
    blockedCallback: ((permissions: Array<String>) -> Unit)? = null,
    rationaleMessage: String? = null
) = activity?.runWithPermissions(
    permissions,
    grantedCallback,
    deniedCallback,
    blockedCallback,
    rationaleMessage
)

fun Activity.runWithPermissions(
    permissions: Array<String>,
    grantedCallback: () -> Unit,
    deniedCallback: (() -> Unit)? = null,
    blockedCallback: ((permissions: Array<String>) -> Unit)? = null,
    rationaleMessage: String? = null,
) = checkPermissions(
    permissions,
    grantedCallback,
    deniedCallback,
    blockedCallback,
    rationaleMessage
)

fun Activity.checkPermissions(
    permissions: Array<String>,
    grantedCallback: (() -> Unit)? = null,
    deniedCallback: (() -> Unit)? = null,
    blockedCallback: ((permissions: Array<String>) -> Unit)? = null,
    rationaleMessage: String? = null
) = if (permissions.all { checkSelfPermission(it) == PackageManager.PERMISSION_GRANTED }) {
    grantedCallback?.invoke()
} else {
    PermissionsActivity.apply {
        sGrantedCallback = grantedCallback
        sBlockedCallback = blockedCallback
        sDeniedCallback = deniedCallback
    }
    startActivity(Intent(this, PermissionsActivity::class.java).also {
        it.putExtra(PermissionsActivity.EXTRA_PERMISSIONS, permissions)
        it.putExtra(PermissionsActivity.EXTRA_RATIONAL_MESSAGE, rationaleMessage)
        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    })
}
//endregion