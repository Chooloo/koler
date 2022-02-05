package com.chooloo.www.chooloolib.ui.base

import androidx.annotation.StringRes
import androidx.lifecycle.Lifecycle

interface BaseContract {
    interface View {
        val controller: Controller<out View>

        fun onSetup()
        fun finish() {}
        fun getLifecycle(): Lifecycle
        fun showError(@StringRes stringResId: Int)
        fun showMessage(@StringRes stringResId: Int)
    }

    interface Controller<V : View> {
        val view: V

        fun init() {}
        fun onSetup()
    }
}