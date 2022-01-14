package com.chooloo.www.chooloolib.ui.base

import androidx.annotation.StringRes
import androidx.lifecycle.Lifecycle
import com.chooloo.www.chooloolib.di.activitycomponent.ActivityComponent

interface BaseContract {
    interface View {
        val component: ActivityComponent

        fun onSetup()
        fun finish() {}
        fun getLifecycle(): Lifecycle
        fun showError(@StringRes stringResId: Int)
        fun showMessage(@StringRes stringResId: Int)
    }

    interface Controller<V : View> {
        val view: V
    }
}