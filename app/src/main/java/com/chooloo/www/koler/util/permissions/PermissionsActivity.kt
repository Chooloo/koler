package com.chooloo.www.koler.util.permissions

import android.R
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings

class PermissionsActivity : Activity() {
    private lateinit var _allPermissions: Array<String>
    private lateinit var _deniedPermissions: Array<String>
    private lateinit var _deniedNonRationalPermissions: Array<String>
    private var _rationaleMessage: String? = null

    companion object {
        private const val RC_PERMISSION = 6937
        private const val RC_SETTINGS = 6739

        const val EXTRA_RATIONAL_MESSAGE = "extra_rationale"
        const val EXTRA_PERMISSIONS = "extra_permissions"

        var listener: PermissionsListener? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFinishOnTouchOutside(true)
        window.statusBarColor = 0

        intent.also {
            _rationaleMessage = it.getStringExtra(EXTRA_RATIONAL_MESSAGE)
            _allPermissions = it.getStringArrayExtra(EXTRA_PERMISSIONS) ?: arrayOf()
        }

        _allPermissions.filter { !hasSelfPermission(it) }.also { denied ->
            _deniedPermissions = denied.toTypedArray()
            _deniedNonRationalPermissions = denied.filter { !shouldShowRequestPermissionRationale(it) }.toTypedArray()
        }

        if (_deniedPermissions.isEmpty()) {
            onGranted()
        } else {
            requestPermissions(_deniedPermissions, RC_PERMISSION)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (grantResults.isEmpty()) {
            onDenied()
            return
        }

        _deniedPermissions = permissions.filterIndexed { index, _ -> checkGrantResult(grantResults[index]) }.toTypedArray()
        if (_deniedPermissions.isEmpty()) {
            onGranted()
            return
        }

        val blockedPermissions = _deniedPermissions.filter { !shouldShowRequestPermissionRationale(it) }.toTypedArray()
        val justBlockedPermissions = blockedPermissions.filter { !_deniedNonRationalPermissions.contains(it) }.toTypedArray()
        when {
            justBlockedPermissions.isNotEmpty() -> onJustBlocked(justBlockedPermissions)
            _deniedPermissions.any { shouldShowRequestPermissionRationale(it) } -> onDenied() // just denied
            listener?.onBlocked(blockedPermissions) == true -> sendToSettings() // blocked
            else -> finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RC_SETTINGS && listener != null) {
            checkPermissions(_allPermissions, null, listener)
        }
        super.finish()
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun finish() {
        listener = null
        super.finish()
    }

    private fun onGranted() {
        val listener = listener
        finish()
        listener?.onGranted()
    }

    private fun onDenied() {
        val listener = listener
        finish()
        listener?.onDenied(_deniedPermissions)
    }

    private fun onJustBlocked(justBlockedPermissions: Array<String>) {
        val listener = listener
        finish()
        listener?.onJustBlocked(justBlockedPermissions)
    }

    private fun sendToSettings() {
        AlertDialog.Builder(this).setTitle("Required Permissions")
                .setMessage(_rationaleMessage)
                .setPositiveButton("Enable") { _, _ ->
                    Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", packageName, null)).also {
                        startActivityForResult(it, RC_SETTINGS)
                    }
                }
                .setNegativeButton(R.string.cancel) { _, _ -> onDenied() }
                .setOnCancelListener { onDenied() }
                .create().show()
    }

    private fun checkGrantResult(grantResult: Int): Boolean {
        return grantResult == PackageManager.PERMISSION_GRANTED
    }
}