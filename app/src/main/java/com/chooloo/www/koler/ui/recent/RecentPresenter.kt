package com.chooloo.www.koler.ui.recent

import com.chooloo.www.koler.ui.base.BasePresenter

class RecentPresenter<V : RecentContract.View> : BasePresenter<V>(), RecentContract.Presenter<V> {
    override fun onActionSms() {
        mvpView?.smsRecent()
    }

    override fun onActionCall() {
        mvpView?.callRecent()
    }

    override fun onActionDelete() {
        mvpView?.deleteRecent()
    }

    override fun onActionAddContact() {
        mvpView?.addContact()
    }

    override fun onActionOpenContact() {
        mvpView?.openContact()
    }

    override fun onActionShowHistory() {
        mvpView?.openHistory()
    }
}