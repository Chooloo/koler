package com.chooloo.www.koler.ui.base

import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.KeyEvent.*
import android.view.View
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.chooloo.www.koler.di.activitycomponent.ActivityComponent

abstract class BaseFragment : Fragment(), BaseContract.View {
    protected val baseActivity by lazy { context as BaseActivity }

    override val activityComponent: ActivityComponent
        get() = baseActivity.activityComponent

    val args: Bundle
        get() = arguments ?: Bundle()


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context !is BaseActivity) {
            throw TypeCastException("Fragment not a child of base activity")
        }
        baseActivity.onAttachFragment(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onSetup()
    }


    //region base view

    override fun showMessage(message: String) {
        baseActivity.showMessage(message)
    }

    override fun showMessage(@StringRes stringResId: Int) {
        baseActivity.showMessage(stringResId)
    }

    override fun showError(message: String) {
        baseActivity.showError(message)
    }

    override fun showError(@StringRes stringResId: Int) {
        baseActivity.showError(getString(stringResId))
    }

    override fun getColor(color: Int): Int {
        return baseActivity.getColor(color)
    }

    override fun hasPermission(permission: String): Boolean {
        return baseActivity.hasPermission(permission)
    }

    override fun hasPermissions(permissions: Array<String>): Boolean {
        return permissions.any { p -> baseActivity.hasPermission(p) }
    }

    //endregion


    fun reattach() {
        childFragmentManager.beginTransaction().detach(this).attach(this).commitNow()
    }

    fun pressBack() {
        baseActivity.apply {
            dispatchKeyEvent(KeyEvent(ACTION_DOWN, KEYCODE_BACK))
            dispatchKeyEvent(KeyEvent(ACTION_UP, KEYCODE_BACK))
        }
    }
}