package com.chooloo.www.chooloolib.ui.base

import androidx.annotation.StringRes

interface BaseView<out VM : BaseViewState> {
    val viewState: VM?

    fun onSetup()
    fun finish() {}
    fun showError(@StringRes stringResId: Int)
    fun showMessage(@StringRes stringResId: Int)
}