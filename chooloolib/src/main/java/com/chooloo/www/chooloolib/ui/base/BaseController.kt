package com.chooloo.www.chooloolib.ui.base

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

open class BaseController<V : BaseContract.View>(
    override val view: V
) :
    BaseContract.Controller<V>,
    LifecycleObserver {

    init {
        view.getLifecycle().addObserver(this)
    }


    override fun onSetup() {}

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    protected open fun onStart() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    protected open fun onResume() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    protected open fun onStop() {
    }
}
