package com.chooloo.www.koler.ui.call

import android.telecom.Call
import android.view.KeyEvent
import com.chooloo.www.koler.ui.base.MvpPresenter

interface CallMvpPresenter<V : CallMvpView> : MvpPresenter<V> {
    fun onAnswerClick()
    fun onRejectClick()
    fun onCallActionsClick()
    fun onDetailsChanged(details: Call.Details)
    fun onStateChanged(state: Int)
}