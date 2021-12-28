package com.chooloo.www.koler.ui.permissions

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import com.chooloo.www.koler.ui.base.BaseActivity

class PermissionsActivity : BaseActivity() {
    private val _rationaleMessage by lazy { intent.getStringExtra(EXTRA_RATIONAL_MESSAGE) }
    private val _allPermissions by lazy { intent.getStringArrayExtra(EXTRA_PERMISSIONS) }

    override val contentView = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFinishOnTouchOutside(true)
        window.statusBarColor = 0

        val deniedPermissions =
            _allPermissions?.filter { !component.permissions.hasSelfPermission(it) }?.toTypedArray()
        if (deniedPermissions?.isEmpty() == true) {
            onGranted()
        } else {
            deniedPermissions?.let { requestPermissions(it, RC_PERMISSION) }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (component.permissions.hasSelfPermissions(permissions as Array<String>)) {
            onGranted()
        }

        val deniedPermissions = permissions.filterIndexed { index, _ ->
            grantResults[index] == PackageManager.PERMISSION_DENIED
        }.toTypedArray()

        val blockedPermissions =
            deniedPermissions.filter { !shouldShowRequestPermissionRationale(it) }.toTypedArray()

        when {
            deniedPermissions.any { !blockedPermissions.contains(it) } -> onDenied() // just denied
            else -> onBlocked(blockedPermissions) // blocked
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RC_SETTINGS) {
            _allPermissions?.let {
                component.permissions.checkPermissions(
                    it,
                    sGrantedCallback,
                    sDeniedCallback,
                    sBlockedCallback,
                    _rationaleMessage
                )
            }
        }
        super.finish()
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onSetup() {
    }

    override fun finish() {
        sBlockedCallback = null
        sGrantedCallback = null
        sDeniedCallback = null
        super.finish()
    }


    private fun onGranted() {
        val callback = sGrantedCallback
        finish()
        callback?.invoke()
    }

    private fun onDenied() {
        val callback = sDeniedCallback
        finish()
        callback?.invoke()
    }

    private fun onBlocked(blockedPermissions: Array<String>) {
        val callback = sBlockedCallback
        finish()
        callback?.invoke(blockedPermissions)
    }


    companion object {
        private const val RC_PERMISSION = 6937
        private const val RC_SETTINGS = 6739

        const val EXTRA_RATIONAL_MESSAGE = "extra_rationale"
        const val EXTRA_PERMISSIONS = "extra_permissions"

        var sGrantedCallback: (() -> Unit)? = null
        var sBlockedCallback: ((permissions: Array<String>) -> Unit)? = null
        var sDeniedCallback: (() -> Unit)? = null
    }
}