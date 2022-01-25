package com.chooloo.www.chooloolib.ui.base

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

open class BaseController<V : BaseContract.View>(
    final override var view: V
) :
    BaseContract.Controller<V>,
    LifecycleObserver {

    init {
        view.getLifecycle().addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    protected open fun onStart() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    protected open fun onStop() {
    }
}
