package com.chooloo.www.koler.interactor.permission

import androidx.annotation.StringRes
import com.chooloo.www.koler.interactor.base.BaseInteractor

interface PermissionInteractor : BaseInteractor<PermissionInteractor.Listener> {
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

    fun runWithPrompt(titleRes: Int, callback: () -> Unit)

    companion object {
        const val RC_DEFAULT_DIALER = 120
        const val RC_READ_CONTACTS = 1
        const val RC_DEFAULT = 2
    }
}