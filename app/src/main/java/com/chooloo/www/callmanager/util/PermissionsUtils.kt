package com.chooloo.www.callmanager.util

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.telecom.TelecomManager
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.chooloo.www.callmanager.ui.base.BaseActivity
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener

const val RC_DEFAULT_DIALER = 0
const val RC_READ_CONTACTS = 1
const val RC_DEFAULT = 2

// default dialer

fun Context.isDefaultDialer(): Boolean {
    return this.getSystemService(TelecomManager::class.java)?.defaultDialerPackage == this.applicationContext?.packageName
}

fun BaseActivity.requestDefaultDialer() {
    val intent = Intent(TelecomManager.ACTION_CHANGE_DEFAULT_DIALER).apply {
        putExtra(TelecomManager.EXTRA_CHANGE_DEFAULT_DIALER_PACKAGE_NAME, this@requestDefaultDialer.packageName)
    }
    this.startActivityForResult(intent, RC_DEFAULT_DIALER)
}

// general permissions

fun Activity.requestPermissionsWithManager(permissions: Array<String>, callback: (() -> Unit?)?) {
    Dexter.withActivity(this).withPermissions(permissions.toList()).withListener(PermissionsManagerListener(this, callback))
}

fun Context.hasSelfPermission(permissions: Array<String>): Boolean {
    return permissions.all { ActivityCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED }
}

fun Context.runWithPermissions(vararg permissions: String, callback: () -> Unit): Any? {
    return runWithPermissionsHandler(this, arrayOf(*permissions), callback)
}

fun Fragment.runWithPermissions(vararg permissions: String, callback: () -> Unit): Any? {
    return runWithPermissionsHandler(this, arrayOf(*permissions), callback)
}

private fun runWithPermissionsHandler(target: Any, permissions: Array<String>, callback: () -> Unit): Any? {
    val activity = when (target) {
        is BaseActivity -> target
        is Fragment -> target.activity as Activity
        else -> throw IllegalStateException("Found " + target::class.java.canonicalName + " : No support from any classes other than AppCompatActivity/Fragment")
    }

    if (activity.hasSelfPermission(permissions)) {
        return callback()
    }
    activity.requestPermissionsWithManager(permissions, callback)
    return null
}

fun Activity.alertPermanentlyDeniedPermissions(permissions: Array<String>?, message: String?) = permissions.let {
    AlertDialog.Builder(this)
            .setMessage(message)
            .setPositiveButton("Settings") { _, _ -> openAppSettings() }
            .setNegativeButton("Cancel") { _, _ -> }
            .show()
}

fun Activity.alertRationaleDeniedPermissions(permissions: Array<String>?, message: String?, callback: (() -> Unit?)?) = permissions.let {
    AlertDialog.Builder(this)
            .setMessage(message)
            .setPositiveButton("Try Again") { _, _ ->
                requestPermissionsWithManager(permissions ?: arrayOf(), callback)
            }
            .setNegativeButton("Cancel") { _, _ -> }
            .show()
}

fun Activity.openAppSettings() {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", packageName, null))
    startActivityForResult(intent, 1)
}

private class PermissionsManagerListener(
        val activity: Activity,
        val callback: (() -> Unit?)?
) : MultiplePermissionsListener {
    override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
        if (report?.isAnyPermissionPermanentlyDenied == false) {
            callback?.invoke()
        } else {
            val permanentlyDeniedPermissions = report?.deniedPermissionResponses?.filter { it.isPermanentlyDenied }?.map { it.permissionName }
            permanentlyDeniedPermissions.let { activity.alertPermanentlyDeniedPermissions(it?.toTypedArray(), "bla") }
        }
    }

    override fun onPermissionRationaleShouldBeShown(permissions: MutableList<PermissionRequest>?, token: PermissionToken?) {
        activity.alertRationaleDeniedPermissions(permissions?.map { it.name }?.toTypedArray(), "blabla", callback)
    }
}