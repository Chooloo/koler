package com.chooloo.www.koler.util.permissions

import android.app.role.RoleManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.telecom.TelecomManager
import android.telecom.TelecomManager.EXTRA_CHANGE_DEFAULT_DIALER_PACKAGE_NAME
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import com.chooloo.www.koler.R

class PermissionsManager(private val _context: Context) {
    val isDefaultDialer: Boolean
        get() = _context.packageName == (_context.getSystemService(Context.TELECOM_SERVICE) as TelecomManager).defaultDialerPackage


    fun checkDefaultDialer(errorMessageRes: Int? = null) {
        if (!isDefaultDialer) {
            requestDefaultDialer()
            errorMessageRes?.let {
                Toast.makeText(_context, _context.getString(errorMessageRes), Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    fun requestDefaultDialer() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val roleManager = _context.getSystemService(Context.ROLE_SERVICE) as RoleManager
            _context.startActivity(roleManager.createRequestRoleIntent(RoleManager.ROLE_DIALER))
        } else {
            val intent = Intent(TelecomManager.ACTION_CHANGE_DEFAULT_DIALER)
            intent.putExtra(EXTRA_CHANGE_DEFAULT_DIALER_PACKAGE_NAME, _context.packageName)
            _context.startActivity(intent)
        }
    }


    fun hasSelfPermission(permission: String) =
        _context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED

    fun hasSelfPermissions(permissions: Array<String>) =
        permissions.all { hasSelfPermission(it) }

    fun checkPermissions(
        permissions: Array<String>,
        grantedCallback: (() -> Unit)? = null,
        deniedCallback: (() -> Unit)? = null,
        blockedCallback: ((permissions: Array<String>) -> Unit)? = null,
        rationaleMessage: String? = null
    ) = if (permissions.all { hasSelfPermission(it) }) {
        grantedCallback?.invoke()
    } else {
        PermissionsActivity.apply {
            sGrantedCallback = grantedCallback
            sBlockedCallback = blockedCallback
            sDeniedCallback = deniedCallback
        }
        val intent = Intent(_context, PermissionsActivity::class.java).apply {
            putExtra(PermissionsActivity.EXTRA_PERMISSIONS, permissions)
            putExtra(PermissionsActivity.EXTRA_RATIONAL_MESSAGE, rationaleMessage)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        _context.startActivity(intent)
    }


    fun runWithPermissions(
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

    fun runWithDefaultDialer(
        @StringRes errorMessageRes: Int? = null,
        callback: () -> Unit
    ) {
        if (!isDefaultDialer) {
            checkDefaultDialer(errorMessageRes)
        } else {
            callback.invoke()
        }
    }

    fun runWithPrompt(titleRes: Int, callback: () -> Unit) {
        AlertDialog.Builder(_context)
            .setCancelable(true)
            .setTitle(_context.getString(titleRes))
            .setPositiveButton(_context.getString(R.string.action_yes)) { _, _ -> callback.invoke() }
            .setNegativeButton(_context.getString(R.string.action_no)) { _, _ -> }
            .create()
            .show()
    }


    companion object {
        const val RC_DEFAULT_DIALER = 120
        const val RC_READ_CONTACTS = 1
        const val RC_DEFAULT = 2
    }
}