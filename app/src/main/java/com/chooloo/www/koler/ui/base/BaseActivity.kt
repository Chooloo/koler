
package com.chooloo.www.koler.ui.base

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.chooloo.www.koler.di.activitycomponent.ActivityComponentImpl

abstract class BaseActivity : AppCompatActivity(), BaseContract.View {
    override val activityComponent by lazy { ActivityComponentImpl(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(activityComponent.preferencesInteractor.accentTheme.theme)
        super.onCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        onSetup()
    }

    override fun onStop() {
        super.onStop()
        activityComponent.disposables.clear()
    }

    override fun onDestroy() {
        super.onDestroy()
        activityComponent.disposables.dispose()
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
        activityComponent.permissionInteractor.hasSelfPermission(permission)

    override fun hasPermissions(permissions: Array<String>) =
        activityComponent.permissionInteractor.hasSelfPermissions(permissions)

    //endregion
}