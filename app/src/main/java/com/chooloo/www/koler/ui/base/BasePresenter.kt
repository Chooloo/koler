package com.chooloo.www.koler.ui.base

import androidx.lifecycle.LifecycleObserver

open class BasePresenter<V : BaseContract.View>(override val mvpView: V) :
    BaseContract.Presenter<V>, LifecycleObserver
