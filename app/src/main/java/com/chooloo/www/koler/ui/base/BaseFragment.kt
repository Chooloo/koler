package com.chooloo.www.koler.ui.base

import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.KeyEvent.*
import android.view.View
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment(), BaseContract.View {
    protected val _activity by lazy { context as BaseActivity }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context !is BaseActivity) {
            throw TypeCastException("Fragment not a child of base activity")
        }
        _activity.onAttachFragment(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onSetup()
    }

    //region permissions
    override fun hasPermission(permission: String) = _activity.hasPermission(permission)

    override fun hasPermissions(permissions: Array<String>) =
        permissions.any { p -> _activity.hasPermission(p) }
    //endregion

    //region messages
    override fun showMessage(message: String) {
        _activity.showMessage(message)
    }

    override fun showMessage(@StringRes stringResId: Int) {
        _activity.showMessage(stringResId)
    }

    override fun showError(message: String) {
        _activity.showError(message)
    }

    override fun showError(@StringRes stringResId: Int) {
        _activity.showError(getString(stringResId))
    }

    override fun getColor(color: Int) = _activity.getColor(color)

    fun reattach() {
        childFragmentManager.beginTransaction().detach(this).attach(this).commitNow()
    }

    fun pressBack() {
        _activity.apply {
            dispatchKeyEvent(KeyEvent(ACTION_DOWN, KEYCODE_BACK))
            dispatchKeyEvent(KeyEvent(ACTION_UP, KEYCODE_BACK))
        }
    }
    //endregion

    protected val argsSafely: Bundle
        get() = arguments ?: Bundle()
}