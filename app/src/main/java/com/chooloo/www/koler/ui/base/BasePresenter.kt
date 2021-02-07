package com.chooloo.www.koler.ui.base

import androidx.lifecycle.LifecycleObserver

open class BasePresenter<V : MvpView> : MvpPresenter<V>, LifecycleObserver {
    @JvmField
    protected var mvpView: V? = null

    override fun attach(mvpView: V) {
        this.mvpView = mvpView
    }

    override fun detach() {
        mvpView = null
    }
}