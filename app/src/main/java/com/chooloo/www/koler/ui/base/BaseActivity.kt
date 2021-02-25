package com.chooloo.www.koler.ui.base

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.PermissionChecker
import androidx.core.content.PermissionChecker.checkSelfPermission
import com.chooloo.www.koler.util.PreferencesManager
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener

abstract class BaseActivity : AppCompatActivity(), MvpView, PermissionListener {
    protected val preferences by lazy { PreferencesManager.getInstance(this) }

    override fun onStart() {
        super.onStart()
        onSetup()
    }

    override fun finish() {
        super<AppCompatActivity>.finish()
    }

    override fun hasPermission(permission: String): Boolean {
        return checkSelfPermission(this, permission) == PermissionChecker.PERMISSION_GRANTED
    }

    override fun hasPermissions(permissions: Array<String>): Boolean {
        return permissions.any(this::hasPermission)
    }

    override fun askForPermission(permission: String, requestCode: Int?) {
        Dexter.withActivity(this).withPermission(permission).withListener(this)
    }

    override fun askForPermissions(permissions: Array<String>, requestCode: Int?) {
        permissions.forEach { permission -> askForPermission(permission, requestCode) }
    }

    override fun showMessage(message: String) {
        // TODO implement a custom scack bar
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun showMessage(stringResId: Int) {
        showMessage(getString(stringResId))
    }

    override fun showError(message: String) {
        // TODO implement a custom scack bar
        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
    }

    override fun showError(stringResId: Int) {
        showError(getString(stringResId))
    }

    override fun onPermissionGranted(response: PermissionGrantedResponse?) {
    }

    override fun onPermissionDenied(response: PermissionDeniedResponse?) {
    }

    override fun onPermissionRationaleShouldBeShown(
        permission: PermissionRequest?,
        token: PermissionToken?
    ) {
    }
}