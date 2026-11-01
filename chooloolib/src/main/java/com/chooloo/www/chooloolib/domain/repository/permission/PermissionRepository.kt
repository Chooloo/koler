package com.chooloo.www.chooloolib.domain.repository.permission

import androidx.annotation.StringRes

interface PermissionRepository {
    val isDefaultDialer: Boolean

    suspend fun entryDefaultDialerResult(granted: Boolean)

    suspend fun checkDefaultDialer(): Boolean
    suspend fun hasPermission(permission: String): Boolean
    suspend fun hasPermissions(permissions: Array<String>): Boolean

    fun runWithDefaultDialer(
        @StringRes errorMessageRes: Int? = null,
        callback: () -> Unit,
    )

    fun runWithDefaultDialer(
        @StringRes errorMessageRes: Int? = null,
        grantedCallback: () -> Unit,
        notGrantedCallback: (() -> Unit)? = null
    )
}
