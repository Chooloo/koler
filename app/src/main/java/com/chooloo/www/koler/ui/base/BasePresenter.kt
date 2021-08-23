package com.chooloo.www.koler.ui.base

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.chooloo.www.koler.di.boundcomponent.BoundComponentRoot

open class BasePresenter<V : BaseContract.View>(final override var view: V) :
    BaseContract.Presenter<V>, LifecycleObserver {

    init {
        boundComponent.addObserver(this)
    }

    protected val boundComponent: BoundComponentRoot
        get() = view.boundComponent

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    protected open fun onStart() {
    }
}
