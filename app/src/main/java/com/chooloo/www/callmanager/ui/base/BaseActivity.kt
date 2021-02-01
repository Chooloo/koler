package com.chooloo.www.callmanager.ui.base

import android.annotation.TargetApi
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.chooloo.www.callmanager.util.PermissionUtils
import com.chooloo.www.callmanager.util.PreferencesManager
import com.chooloo.www.callmanager.util.Utilities

abstract class BaseActivity : AppCompatActivity(), MvpView {
    protected var preferences: PreferencesManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Utilities.setUpLocale(this)
        preferences = PreferencesManager.getInstance(this)
    }

    override fun onStart() {
        super.onStart()
        onSetup()
    }

    override fun hasPermission(permission: String): Boolean {
        return checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
    }

    override fun hasPermissions(permissions: Array<String>): Boolean {
        return permissions.filter { p -> hasPermission(p) }.isNotEmpty()
    }

    override fun askForPermission(permission: String, requestCode: Int) {
        requestPermissions(arrayOf(permission), requestCode)
    }

    @TargetApi(Build.VERSION_CODES.M)
    override fun askForPermissions(permissions: Array<String>, requestCode: Int) {
        requestPermissions(permissions, requestCode)
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