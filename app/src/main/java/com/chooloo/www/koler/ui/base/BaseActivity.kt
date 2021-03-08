package com.chooloo.www.koler.ui.base

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.PermissionChecker
import androidx.core.content.PermissionChecker.checkSelfPermission
import com.karumi.dexter.listener.single.PermissionListener

abstract class BaseActivity : AppCompatActivity(), BaseContract.View {
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