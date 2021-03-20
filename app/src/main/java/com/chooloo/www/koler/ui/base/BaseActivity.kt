package com.chooloo.www.koler.ui.base

import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.PermissionChecker
import androidx.core.content.PermissionChecker.checkSelfPermission

abstract class BaseActivity : AppCompatActivity(), BaseContract.View {
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
    }
    
    override fun onStart() {
        super.onStart()
        onSetup()
    }

    override fun finish() {
        super<AppCompatActivity>.finish()
    }

    //region permissions
    override fun hasPermission(permission: String) =
        checkSelfPermission(this, permission) == PermissionChecker.PERMISSION_GRANTED

    override fun hasPermissions(permissions: Array<String>) = permissions.any(this::hasPermission)
    //endregion

    //region messages
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
    //endregion

}