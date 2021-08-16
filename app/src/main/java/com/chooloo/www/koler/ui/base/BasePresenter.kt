package com.chooloo.www.koler.ui.base

import androidx.lifecycle.LifecycleObserver
import com.chooloo.www.koler.di.boundcomponent.BoundComponentRoot

open class BasePresenter<V : BaseContract.View>(
    override val mvpView: V
) : BaseContract.Presenter<V>, LifecycleObserver {

    protected val boundComponent: BoundComponentRoot
        get() = mvpView.boundComponent
}
