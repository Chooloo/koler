package com.chooloo.www.koler.ui.base

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.chooloo.www.koler.KolerApp
import com.chooloo.www.koler.di.boundcomponent.BoundComponentRootImpl

abstract class BaseActivity : AppCompatActivity(), BaseContract.View {
    protected val componentRoot by lazy { (applicationContext as KolerApp).componentRoot }
    protected val boundComponentRoot by lazy { BoundComponentRootImpl(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(componentRoot.preferencesInteractor.accentTheme.theme)
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

    override fun hasPermission(permission: String) =
        componentRoot.permissionInteractor.hasSelfPermission(permission)

    override fun hasPermissions(permissions: Array<String>) =
        componentRoot.permissionInteractor.hasSelfPermissions(permissions)

    //endregion
}