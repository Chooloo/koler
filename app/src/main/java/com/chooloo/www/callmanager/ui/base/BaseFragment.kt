package com.chooloo.www.callmanager.ui.base

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment(), MvpView {
    protected lateinit var requiredPermissions: Array<String>
    protected lateinit var activity: BaseActivity

    companion object {
        private const val PERMISSION_RC = 10
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context !is BaseActivity) {
            throw TypeCastException("Fragment not a child of base activity")
        }
        activity = context
        activity.onAttachFragment(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requiredPermissions = onGetPermissions()
        onSetup()
    }

    override fun onGetPermissions(): Array<String> {
        return arrayOf()
    }

    override fun hasPermission(permission: String): Boolean {
        return false
    }

    override fun hasPermissions(): Boolean {
        return requiredPermissions.filter { p -> activity.hasPermission(p) }.isNotEmpty()
    }

    override fun askForPermission(permission: String, requestCode: Int) {
        askForPermissions(arrayOf(permission), requestCode)
    }

    override fun askForPermissions(permissions: Array<String>, requestCode: Int) {
        requestPermissions(permissions, requestCode)
    }

    override fun askForPermissions() {
        askForPermissions(requiredPermissions, PERMISSION_RC)
    }

    override fun showMessage(message: String) {
        activity.showMessage(message)
    }

    override fun showMessage(@StringRes stringResId: Int) {
        activity.showMessage(stringResId)
    }

    override fun showError(message: String) {
        activity.showError(message)
    }

    override fun showError(@StringRes stringResId: Int) {
        activity.showError(getString(stringResId))
    }

    protected val argsSafely: Bundle
        get() = super.getArguments()
                ?: throw IllegalArgumentException("You must create this fragment with newInstance()")
}