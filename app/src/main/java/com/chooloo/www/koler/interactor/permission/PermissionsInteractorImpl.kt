package com.chooloo.www.koler.interactor.permission

import android.Manifest.permission.*
import android.app.role.RoleManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.telecom.TelecomManager
import android.widget.Toast
import com.chooloo.www.koler.interactor.string.StringInteractor
import com.chooloo.www.koler.ui.base.BaseActivity
import com.chooloo.www.koler.ui.permissions.PermissionsActivity
import com.chooloo.www.koler.util.baseobservable.BaseObservable


class PermissionsInteractorImpl(
    private val activity: BaseActivity,
    private val strings: StringInteractor,
    private val telecomManager: TelecomManager
) :
    BaseObservable<PermissionsInteractor.Listener>(),
    PermissionsInteractor {

    override val isDefaultDialer: Boolean
        get() = activity.packageName == telecomManager.defaultDialerPackage

    override fun requestDefaultDialer() {
        val intent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            (activity.getSystemService(Context.ROLE_SERVICE) as RoleManager).createRequestRoleIntent(
                RoleManager.ROLE_DIALER
            )
        } else {
            Intent(TelecomManager.ACTION_CHANGE_DEFAULT_DIALER).putExtra(
                TelecomManager.EXTRA_CHANGE_DEFAULT_DIALER_PACKAGE_NAME,
                activity.packageName
            )
        }
        activity.startActivityForResult(intent, 0)
    }

    override fun checkDefaultDialer(errorMessageRes: Int?) {
        if (!isDefaultDialer) {
            requestDefaultDialer()
            errorMessageRes?.let {
                Toast.makeText(
                    activity,
                    strings.getString(errorMessageRes),
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        }
    }

    override fun hasSelfPermission(permission: String) =
        activity.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED

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
            val intent = Intent(activity, PermissionsActivity::class.java).apply {
                putExtra(PermissionsActivity.EXTRA_PERMISSIONS, permissions)
                putExtra(PermissionsActivity.EXTRA_RATIONAL_MESSAGE, rationaleMessage)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            activity.startActivity(intent)
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

    override fun runWithDefaultDialer(
        errorMessageRes: Int?,
        grantedCallback: () -> Unit,
        notGrantedCallback: (() -> Unit)?
    ) {
        if (!isDefaultDialer) {
            checkDefaultDialer(errorMessageRes)
            notGrantedCallback?.invoke()
        } else {
            grantedCallback.invoke()
        }
    }

    override fun runWithReadCallLogPermissions(callback: (granted: Boolean) -> Unit) {
        runWithPermissions(arrayOf(READ_CALL_LOG),
            grantedCallback = { callback.invoke(true) },
            deniedCallback = { callback.invoke(false) },
            blockedCallback = { callback.invoke(false) })
    }

    override fun runWithReadContactsPermissions(callback: (granted: Boolean) -> Unit) {
        runWithPermissions(arrayOf(READ_CONTACTS),
            grantedCallback = { callback.invoke(true) },
            deniedCallback = { callback.invoke(false) },
            blockedCallback = { callback.invoke(false) })
    }

    override fun runWithWriteContactsPermissions(callback: (granted: Boolean) -> Unit) {
        runWithPermissions(arrayOf(WRITE_CONTACTS),
            grantedCallback = { callback.invoke(true) },
            deniedCallback = { callback.invoke(false) },
            blockedCallback = { callback.invoke(false) })
    }

    override fun runWithWriteCallLogPermissions(callback: (granted: Boolean) -> Unit) {
        runWithPermissions(arrayOf(WRITE_CALL_LOG),
            grantedCallback = { callback.invoke(true) },
            deniedCallback = { callback.invoke(false) },
            blockedCallback = { callback.invoke(false) })
    }
}