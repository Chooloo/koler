package com.chooloo.www.koler.ui.recent

import com.chooloo.www.koler.ui.base.BasePresenter

class RecentPresenter<V : RecentContract.View>(mvpView: V) :
    BasePresenter<V>(mvpView),
    RecentContract.Presenter<V> {
    
    override fun onActionMenu() {
        view.showMenu()
    }

    override fun onActionSms() {
        view.smsRecent()
    }

    override fun onActionCall() {
        view.callRecent()
    }

    override fun onActionDelete() {
        view.deleteRecent()
    }

    override fun onActionAddContact() {
        view.addContact()
    }

    override fun onActionOpenContact() {
        view.openContact()
    }

    override fun onActionShowHistory() {
        view.openHistory()
    }
}