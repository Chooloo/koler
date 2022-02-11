package com.chooloo.www.chooloolib.interactor.permission

import android.Manifest.permission.*
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.content.pm.PackageManager
import android.telecom.TelecomManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.chooloo.www.chooloolib.ui.permissions.DefaultDIalerRequestActivity
import com.chooloo.www.chooloolib.ui.permissions.PermissionRequestActivity
import com.chooloo.www.chooloolib.ui.permissions.PermissionRequestActivity.Companion.PERMISSIONS_ARGUMENT_KEY
import com.chooloo.www.chooloolib.ui.permissions.PermissionRequestActivity.Companion.PermissionResult
import com.chooloo.www.chooloolib.ui.permissions.PermissionRequestActivity.Companion.PermissionState.GRANTED
import com.chooloo.www.chooloolib.ui.permissions.PermissionRequestActivity.Companion.REQUEST_CODE_ARGUMENT_KEY
import com.chooloo.www.chooloolib.util.baseobservable.BaseObservable
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PermissionsInteractorImpl @Inject constructor(
    private val telecomManager: TelecomManager,
    @ApplicationContext private val context: Context
) :
    BaseObservable<PermissionsInteractor.Listener>(),
    PermissionsInteractor {

    override val isDefaultDialer: Boolean
        get() = context.packageName == telecomManager.defaultDialerPackage

    private val _onPermissionsResultListeners =
        ConcurrentHashMap<Int, (List<PermissionResult>) -> Unit>(1)

    private val _onDefaultDialerResultListeners = mutableListOf<(Boolean) -> Unit>()
    private var _requestCode = 255
        get() {
            _requestCode = field--
            return if (field < 0) 255 else field
        }

    override fun entryPermissionResult(
        responses: List<PermissionResult>,
        requestCode: Int
    ) {
        _onPermissionsResultListeners[requestCode]?.invoke(responses)
        _onPermissionsResultListeners.remove(requestCode)
    }


    override fun entryDefaultDialerResult(granted: Boolean) {
        _onDefaultDialerResultListeners.forEach { it.invoke(granted) }
        _onDefaultDialerResultListeners.clear()
    }

    override fun hasSelfPermission(permission: String) =
        ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED

    override fun hasSelfPermissions(permissions: Array<String>) =
        permissions.all { hasSelfPermission(it) }

    override fun checkDefaultDialer(callback: (Boolean) -> Unit) {
        if (isDefaultDialer) {
            callback.invoke(true)
        } else {
            _onDefaultDialerResultListeners.add(callback)
            val intent = Intent(context, DefaultDIalerRequestActivity::class.java)
                .addFlags(FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }

    override fun checkPermissions(
        vararg permissions: String,
        callback: (List<PermissionResult>) -> Unit
    ) {
        val intent = Intent(context, PermissionRequestActivity::class.java)
            .putExtra(PERMISSIONS_ARGUMENT_KEY, permissions)
            .putExtra(REQUEST_CODE_ARGUMENT_KEY, _requestCode)
            .addFlags(FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
        _onPermissionsResultListeners[_requestCode] = callback
    }

    override fun runWithPermissions(
        permissions: Array<String>,
        grantedCallback: () -> Unit,
        deniedCallback: (() -> Unit)?
    ) {
        if (permissions.all(this::hasSelfPermission)) {
            grantedCallback.invoke()
        } else {
            checkPermissions(*permissions) {
                if (it.all { a -> a.state == GRANTED }) {
                    grantedCallback.invoke()
                } else {
                    deniedCallback?.invoke()
                }
            }
        }
    }

    override fun runWithDefaultDialer(errorMessageRes: Int?, callback: () -> Unit) {
        checkDefaultDialer {
            if (it) {
                callback.invoke()
            } else {
                errorMessageRes?.let { it1 ->
                    Toast.makeText(
                        context,
                        it1, Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    override fun runWithDefaultDialer(
        errorMessageRes: Int?,
        grantedCallback: () -> Unit,
        notGrantedCallback: (() -> Unit)?
    ) {
        checkDefaultDialer {
            if (it) {
                grantedCallback.invoke()
            } else {
                errorMessageRes?.let { it1 ->
                    Toast.makeText(
                        context,
                        it1, Toast.LENGTH_SHORT
                    ).show()
                }
                notGrantedCallback?.invoke()
            }
        }
    }

    override fun runWithReadCallLogPermissions(callback: (Boolean) -> Unit) {
        runWithPermissions(
            arrayOf(READ_CALL_LOG),
            grantedCallback = { callback.invoke(true) },
            deniedCallback = { callback.invoke(false) }
        )
    }

    override fun runWithReadContactsPermissions(callback: (Boolean) -> Unit) {
        runWithPermissions(
            arrayOf(READ_CONTACTS),
            grantedCallback = { callback.invoke(true) },
            deniedCallback = { callback.invoke(false) }
        )
    }

    override fun runWithWriteContactsPermissions(callback: (Boolean) -> Unit) {
        runWithPermissions(
            arrayOf(WRITE_CONTACTS),
            grantedCallback = { callback.invoke(true) },
            deniedCallback = { callback.invoke(false) }
        )
    }

    override fun runWithWriteCallLogPermissions(callback: (Boolean) -> Unit) {
        runWithPermissions(
            arrayOf(WRITE_CALL_LOG),
            grantedCallback = { callback.invoke(true) },
            deniedCallback = { callback.invoke(false) }
        )
    }

    override fun runWithWriteCallPhonePermissions(callback: (Boolean) -> Unit) {
        runWithPermissions(
            arrayOf(CALL_PHONE),
            grantedCallback = { callback.invoke(true) },
            deniedCallback = { callback.invoke(false) }
        )
    }

    override fun runWithWriteReadPhoneStatePermissions(callback: (Boolean) -> Unit) {
        runWithPermissions(
            arrayOf(READ_PHONE_STATE),
            grantedCallback = { callback.invoke(true) },
            deniedCallback = { callback.invoke(false) }
        )
    }
}