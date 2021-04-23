package com.chooloo.www.koler.ui.base

import android.os.Bundle
import androidx.annotation.ColorRes
import androidx.annotation.StringRes

interface BaseContract {
    interface View {
        fun onSetup()
        fun finish() {}
        fun hasPermission(permission: String): Boolean
        fun hasPermissions(permissions: Array<String>): Boolean
        fun showMessage(message: String)
        fun showMessage(@StringRes stringResId: Int)
        fun showError(message: String)
        fun showError(@StringRes stringResId: Int)
        fun getString(@StringRes resId: Int): String
        fun getColor(@ColorRes color: Int): Int
    }

    interface Presenter<V : View> {
        fun attach(mvpView: V)
        fun detach()
    }
}