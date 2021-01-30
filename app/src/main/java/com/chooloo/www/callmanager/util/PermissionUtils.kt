package com.chooloo.www.callmanager.util

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.telecom.TelecomManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.chooloo.www.callmanager.ui.base.BaseActivity

object PermissionUtils {
    const val RC_DEFAULT = 0
    const val RC_WAKE_LOCK = 1
    const val RC_MANAGE_OWN_CALLS = 2
    const val RC_CALL_PHONE = 3
    const val RC_READ_PHONE_STATE = 4
    const val RC_READ_CONTACTS = 5
    const val RC_READ_CALL_LOG = 6
    const val RC_WRITE_CALL_LOG = 7
    const val RC_SEND_SMS = 8
    const val RC_WRITE_CONTACTS = 9
    const val RC_VIBRATE = 10
    const val RC_MODIFY_AUDIO_SETTINGS = 11
    const val RC_READ_EXTERNAL_STORAGE = 12
    const val RC_WRITE_EXTERNAL_STORAGE = 13
    const val RC_RECORD_AUDIO = 14
    const val RC_USE_BIOMETRIC = 15
    const val RC_DEFAULT_DIALER = 16

    fun isDefaultDialer(activity: BaseActivity): Boolean {
        return activity.getSystemService(TelecomManager::class.java).defaultDialerPackage == activity.application.packageName
    }

    @JvmStatic
    fun ensureDefaultDialer(activity: BaseActivity) {
        if (!isDefaultDialer(activity)) {
            val intent = Intent(TelecomManager.ACTION_CHANGE_DEFAULT_DIALER).apply {
                putExtra(TelecomManager.EXTRA_CHANGE_DEFAULT_DIALER_PACKAGE_NAME, activity.packageName)
            }
            activity.startActivityForResult(intent, RC_DEFAULT_DIALER)
        }
    }

    fun checkPermission(context: Context, permission: String, askForIt: Boolean): Boolean {
        val isGranted = ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
        if (askForIt && !isGranted && context is BaseActivity) {
            ActivityCompat.requestPermissions(context, arrayOf(permission), RC_DEFAULT)
        }
        return isGranted
    }

    @JvmStatic
    fun checkPermissionsGranted(grantResults: IntArray): Boolean {
        return !grantResults.contains(PackageManager.PERMISSION_DENIED)
    }
}