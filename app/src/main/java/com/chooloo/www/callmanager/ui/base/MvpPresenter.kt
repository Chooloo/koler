package com.chooloo.www.callmanager.ui.base

interface MvpPresenter<V : MvpView> {

    fun attach(mvpView: V)
    fun detach()
}