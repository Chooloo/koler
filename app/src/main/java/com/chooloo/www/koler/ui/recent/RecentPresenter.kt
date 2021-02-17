package com.chooloo.www.koler.ui.recent

import com.chooloo.www.koler.ui.base.BasePresenter

class RecentPresenter<V : RecentMvpView> : BasePresenter<V>(), RecentMvpPresenter<V> {

    override fun onActionCall() {
        mvpView?.call()
    }

    override fun onActionSms() {
        mvpView?.sms()
    }

    override fun onActionDelete() {
        mvpView?.delete()
    }
}