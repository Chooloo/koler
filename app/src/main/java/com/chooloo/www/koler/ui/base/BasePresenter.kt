package com.chooloo.www.koler.ui.base

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

open class BasePresenter<V : BaseContract.View>(final override var view: V) :
    BaseContract.Presenter<V>, LifecycleObserver {

    init {
        view.getLifecycle().addObserver(this)
    }

    protected val boundComponent get() = view.boundComponent

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    protected open fun onStart() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    protected open fun onStop() {
    }
}
