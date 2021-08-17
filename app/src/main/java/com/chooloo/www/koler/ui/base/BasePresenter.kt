package com.chooloo.www.koler.ui.base

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import com.chooloo.www.koler.di.boundcomponent.BoundComponentRoot

open class BasePresenter<V : BaseContract.View>(
    final override var view: V,
    final override var viewLifecycle: Lifecycle
) : BaseContract.Presenter<V>, LifecycleObserver {

    init {
        viewLifecycle.addObserver(this)
    }

    protected val boundComponent: BoundComponentRoot
        get() = view.boundComponent
}
