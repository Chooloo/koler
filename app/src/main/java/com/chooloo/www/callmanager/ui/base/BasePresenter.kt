package com.chooloo.www.callmanager.ui.base

import androidx.lifecycle.LifecycleObserver

open class BasePresenter<V : MvpView?> : MvpPresenter<V>, LifecycleObserver {
    @JvmField protected var mMvpView: V? = null

    override fun attach(mvpView: V) {
        mMvpView = mvpView
    }

    override fun detach() {
        mMvpView = null
    }
}