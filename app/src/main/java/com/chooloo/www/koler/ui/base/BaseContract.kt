package com.chooloo.www.koler.ui.base

import androidx.annotation.StringRes
import androidx.lifecycle.Lifecycle
import com.chooloo.www.koler.di.activitycomponent.ActivityComponent

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