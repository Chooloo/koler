package com.chooloo.www.koler.ui.recent

import com.chooloo.www.koler.ui.base.BasePresenter

class RecentPresenter<V : RecentContract.View> : BasePresenter<V>(), RecentContract.Presenter<V> {
    override fun onActionCall() {
        mvpView?.callRecent()
    }

    override fun onActionSms() {
        mvpView?.smsRecent()
    }

    override fun onActionDelete() {
        mvpView?.deleteRecent()
    }
}