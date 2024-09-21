package com.chooloo.www.chooloolib.domain.repository.permission

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.content.pm.PackageManager
import android.telecom.TelecomManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.chooloo.www.chooloolib.domain.repository.base.BaseRepositoryImpl
import com.chooloo.www.chooloolib.utils.DefaultDialerRequestActivity
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume

@Singleton
class PermissionRepositoryImpl @Inject constructor(
    private val telecomManager: TelecomManager,
    @ApplicationContext private val context: Context
) : BaseRepositoryImpl(), PermissionRepository {
    private val _onDefaultDialerResultListeners = mutableListOf<(Boolean) -> Unit>()

    override val isDefaultDialer
        get() = context.packageName == telecomManager.defaultDialerPackage


    private fun checkDefaultDialer(callback: (Boolean) -> Unit) {
        if (isDefaultDialer) {
            callback.invoke(true)
        } else {
            _onDefaultDialerResultListeners.add(callback)
            val intent = Intent(context, DefaultDialerRequestActivity::class.java)
                .addFlags(FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }

    override suspend fun entryDefaultDialerResult(granted: Boolean) {
        _onDefaultDialerResultListeners.forEach { it.invoke(granted) }
        _onDefaultDialerResultListeners.clear()
    }

    override suspend fun checkDefaultDialer(): Boolean =
        suspendCancellableCoroutine { continuation ->
            checkDefaultDialer(continuation::resume)
        }

    override suspend fun hasPermission(permission: String) =
        ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED

    override suspend fun hasPermissions(permissions: Array<String>) =
        permissions.all { hasPermission(it) }

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
}