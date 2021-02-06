package com.chooloo.www.callmanager.ui.base

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.PermissionChecker
import androidx.core.content.PermissionChecker.checkSelfPermission
import com.chooloo.www.callmanager.util.PreferencesManager
import com.karumi.dexter.Dexter

abstract class BaseActivity : AppCompatActivity(), MvpView {
    protected var preferences: PreferencesManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preferences = PreferencesManager.getInstance(this)
    }

    override fun onStart() {
        super.onStart()
        onSetup()
    }

    override fun hasPermission(permission: String): Boolean {
        return checkSelfPermission(this, permission) == PermissionChecker.PERMISSION_GRANTED
    }

    override fun hasPermissions(permissions: Array<String>): Boolean {
        return permissions.any { p -> hasPermission(p) }
    }

    override fun askForPermission(permission: String, requestCode: Int?) {
        Dexter.withActivity(this).withPermission(permission)
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
}