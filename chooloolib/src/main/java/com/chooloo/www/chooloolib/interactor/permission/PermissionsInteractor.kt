package com.chooloo.www.chooloolib.interactor.permission

import androidx.annotation.StringRes
import com.chooloo.www.chooloolib.interactor.base.BaseInteractor

interface PermissionsInteractor : BaseInteractor<PermissionsInteractor.Listener> {
    interface Listener

    val isDefaultDialer: Boolean

    fun requestDefaultDialer()
    fun checkDefaultDialer(@StringRes errorMessageRes: Int? = null)

    fun hasSelfPermission(permission: String): Boolean
    fun hasSelfPermissions(permissions: Array<String>): Boolean

    fun checkPermissions(
        permissions: Array<String>,
        grantedCallback: (() -> Unit)? = null,
        deniedCallback: (() -> Unit)? = null,
        blockedCallback: ((permissions: Array<String>) -> Unit)? = null,
        rationaleMessage: String? = null
    )

    fun runWithPermissions(
        permissions: Array<String>,
        grantedCallback: () -> Unit,
        deniedCallback: (() -> Unit)? = null,
        blockedCallback: ((permissions: Array<String>) -> Unit)? = null,
        rationaleMessage: String? = null,
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

    fun runWithReadCallLogPermissions(callback: (granted: Boolean) -> Unit)
    fun runWithReadContactsPermissions(callback: (granted: Boolean) -> Unit)
    fun runWithWriteContactsPermissions(callback: (granted: Boolean) -> Unit)
    fun runWithWriteCallLogPermissions(callback: (granted: Boolean) -> Unit)

    companion object {
        const val RC_DEFAULT = 2
    }
}