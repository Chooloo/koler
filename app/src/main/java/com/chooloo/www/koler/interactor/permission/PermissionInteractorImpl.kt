package com.chooloo.www.koler.interactor.permission

import android.app.role.RoleManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.telecom.TelecomManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.chooloo.www.koler.R
import com.chooloo.www.koler.util.BaseObservable
import com.chooloo.www.koler.util.permissions.PermissionsActivity


class PermissionInteractorImpl(
    private val context: Context,
    private val telecomManager: TelecomManager
) :
    BaseObservable<PermissionInteractor.Listener>(),
    PermissionInteractor {

    override val isDefaultDialer: Boolean
        get() = context.packageName == telecomManager.defaultDialerPackage

    override fun requestDefaultDialer() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val roleManager = context.getSystemService(Context.ROLE_SERVICE) as RoleManager
            context.startActivity(roleManager.createRequestRoleIntent(RoleManager.ROLE_DIALER))
        } else {
            val intent = Intent(TelecomManager.ACTION_CHANGE_DEFAULT_DIALER)
            intent.putExtra(
                TelecomManager.EXTRA_CHANGE_DEFAULT_DIALER_PACKAGE_NAME,
                context.packageName
            )
            context.startActivity(intent)
        }
    }

    override fun checkDefaultDialer(errorMessageRes: Int?) {
        if (!isDefaultDialer) {
            requestDefaultDialer()
            errorMessageRes?.let {
                Toast.makeText(context, context.getString(errorMessageRes), Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    override fun hasSelfPermission(permission: String) =
        context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED

    override fun hasSelfPermissions(permissions: Array<String>) =
        permissions.all { hasSelfPermission(it) }

    override fun checkPermissions(
        permissions: Array<String>,
        grantedCallback: (() -> Unit)?,
        deniedCallback: (() -> Unit)?,
        blockedCallback: ((permissions: Array<String>) -> Unit)?,
        rationaleMessage: String?
    ) {
        if (permissions.all { hasSelfPermission(it) }) {
            grantedCallback?.invoke()
        } else {
            PermissionsActivity.apply {
                sGrantedCallback = grantedCallback
                sBlockedCallback = blockedCallback
                sDeniedCallback = deniedCallback
            }
            val intent = Intent(context, PermissionsActivity::class.java).apply {
                putExtra(PermissionsActivity.EXTRA_PERMISSIONS, permissions)
                putExtra(PermissionsActivity.EXTRA_RATIONAL_MESSAGE, rationaleMessage)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        }
    }

    override fun runWithPermissions(
        permissions: Array<String>,
        grantedCallback: () -> Unit,
        deniedCallback: (() -> Unit)?,
        blockedCallback: ((permissions: Array<String>) -> Unit)?,
        rationaleMessage: String?
    ) = checkPermissions(
        permissions,
        grantedCallback,
        deniedCallback,
        blockedCallback,
        rationaleMessage
    )

    override fun runWithDefaultDialer(errorMessageRes: Int?, callback: () -> Unit) {
        if (!isDefaultDialer) {
            checkDefaultDialer(errorMessageRes)
        } else {
            callback.invoke()
        }
    }

    override fun runWithPrompt(titleRes: Int, callback: () -> Unit) {
        AlertDialog.Builder(context)
            .setCancelable(true)
            .setTitle(context.getString(titleRes))
            .setPositiveButton(context.getString(R.string.action_yes)) { _, _ -> callback.invoke() }
            .setNegativeButton(context.getString(R.string.action_no)) { _, _ -> }
            .create()
            .show()
    }
}