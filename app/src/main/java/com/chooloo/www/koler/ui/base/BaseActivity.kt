package com.chooloo.www.koler.ui.base

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.PermissionChecker
import androidx.core.content.PermissionChecker.checkSelfPermission
import com.chooloo.www.koler.util.preferences.KolerPreferences

abstract class BaseActivity : AppCompatActivity(), BaseContract.View {
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(KolerPreferences(this).accentTheme.theme)
        super.onCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        onSetup()
    }


    //region base view

    override fun finish() {
        super<AppCompatActivity>.finish()
    }

    override fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun showMessage(stringResId: Int) {
        showMessage(getString(stringResId))
    }

    override fun showError(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
    }

    override fun showError(stringResId: Int) {
        showError(getString(stringResId))
    }

    override fun hasPermission(permission: String): Boolean {
        return checkSelfPermission(this, permission) == PermissionChecker.PERMISSION_GRANTED
    }

    override fun hasPermissions(permissions: Array<String>): Boolean {
        return permissions.any(this::hasPermission)
    }

    //endregion
}