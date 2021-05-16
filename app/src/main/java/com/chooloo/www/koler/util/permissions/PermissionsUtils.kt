package com.chooloo.www.koler.util.permissions

import android.app.Activity
import android.content.Context
import android.content.Context.TELECOM_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.telecom.TelecomManager
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.chooloo.www.koler.R
import com.chooloo.www.koler.ui.base.BaseActivity

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

fun BaseActivity.runWithDefaultDialer(@StringRes errorMessageRes: Int? = null, callback: () -> Unit) {
    if (!isDefaultDialer()) {
        showError(getString(errorMessageRes ?: R.string.error_not_default_dialer))
        requestDefaultDialer()
    } else {
        callback.invoke()
    }
}

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
    val intent = Intent(this, PermissionsActivity::class.java).apply {
        putExtra(PermissionsActivity.EXTRA_PERMISSIONS, permissions)
        putExtra(PermissionsActivity.EXTRA_RATIONAL_MESSAGE, rationaleMessage)
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
    }
    startActivity(intent)
}

fun Context.runWithPrompt(titleRes: Int, callback: () -> Unit) {
    AlertDialog.Builder(this)
        .setCancelable(true)
        .setTitle(getString(titleRes))
        .setPositiveButton(getString(R.string.action_yes)) { _, _ -> callback.invoke() }
        .setNegativeButton(getString(R.string.action_no)) { _, _ -> }
        .create()
        .show()
}
//endregion