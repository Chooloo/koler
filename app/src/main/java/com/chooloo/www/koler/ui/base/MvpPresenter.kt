package com.chooloo.www.koler.ui.base

interface MvpPresenter<V : MvpView> {
    fun attach(mvpView: V)
    fun detach()
}