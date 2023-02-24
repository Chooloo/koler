package com.chooloo.www.chooloolib.interactor.permission

import androidx.annotation.StringRes
import com.chooloo.www.chooloolib.interactor.base.BaseInteractor
import com.chooloo.www.chooloolib.ui.permissions.PermissionRequestActivity.Companion.PermissionResult

interface PermissionsInteractor : BaseInteractor<PermissionsInteractor.Listener> {
    interface Listener

    val isDefaultDialer: Boolean


    fun entryDefaultDialerResult(granted: Boolean)
    fun entryPermissionResult(responses: List<PermissionResult>, requestCode: Int)

    fun hasSelfPermission(permission: String): Boolean
    fun hasSelfPermissions(permissions: Array<String>): Boolean

    fun checkDefaultDialer(callback: (Boolean) -> Unit)
    fun checkPermissions(
        vararg permissions: String,
        callback: (List<PermissionResult>) -> Unit
    )

    suspend fun checkPermission(permission: String): Boolean
    suspend fun checkPermissions(vararg permissions: String): Boolean

    fun runWithPermissions(
        permissions: Array<String>,
        grantedCallback: () -> Unit,
        deniedCallback: (() -> Unit)? = null
    )

    fun runWithPermissions(
        vararg permissions: String,
        callback: (Boolean) -> Unit
    )


    fun runWithDefaultDialer(
        @StringRes errorMessageRes: Int? = null,
        callback: () -> Unit,
    )

    fun runWithDefaultDialer(
        @StringRes errorMessageRes: Int? = null,
        grantedCallback: () -> Unit,
        notGrantedCallback: (() -> Unit)? = null
    )

    fun runWithReadCallLogPermissions(callback: (Boolean) -> Unit)
    fun runWithReadContactsPermissions(callback: (Boolean) -> Unit)
    fun runWithWriteContactsPermissions(callback: (Boolean) -> Unit)
    fun runWithWriteCallLogPermissions(callback: (Boolean) -> Unit)
    fun runWithWriteCallPhonePermissions(callback: (Boolean) -> Unit)
    fun runWithWriteReadPhoneStatePermissions(callback: (Boolean) -> Unit)
}