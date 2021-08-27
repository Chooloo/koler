package com.chooloo.www.koler.ui.base

import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.lifecycle.Lifecycle
import com.chooloo.www.koler.di.boundcomponent.BoundComponentRoot

interface BaseContract {
    interface View {
        val boundComponent: BoundComponentRoot

        fun onSetup()
        fun finish() {}
        fun getLifecycle(): Lifecycle
        fun showMessage(message: String)
        fun showMessage(@StringRes stringResId: Int)
        fun showError(message: String)
        fun showError(@StringRes stringResId: Int)
        fun getColor(@ColorRes color: Int): Int
        fun getString(@StringRes resId: Int): String
        fun hasPermission(permission: String): Boolean
        fun hasPermissions(permissions: Array<String>): Boolean
    }

    interface Presenter<V : View> {
        val view: V
    }
}