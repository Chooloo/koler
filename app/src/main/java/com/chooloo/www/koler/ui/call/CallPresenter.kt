package com.chooloo.www.koler.ui.call

import android.telecom.Call
import com.chooloo.www.koler.ui.base.BasePresenter

class CallPresenter<V : CallMvpView> : BasePresenter<V>(), CallMvpPresenter<V> {
    override fun onAnswerClick() {
        mvpView?.answer()
    }

    override fun onRejectClick() {
        mvpView?.reject()
    }

    override fun onCallActionsClick() {
        mvpView?.openCallActions()
    }

    override fun onDetailsChanged(details: Call.Details) {
        mvpView?.updateDetails(details)
    }

    override fun onStateChanged(state: Int) {
        mvpView?.updateState(state)
    }
}