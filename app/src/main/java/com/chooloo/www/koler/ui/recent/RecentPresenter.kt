package com.chooloo.www.koler.ui.recent

import androidx.lifecycle.Lifecycle
import com.chooloo.www.koler.ui.base.BasePresenter

class RecentPresenter<V : RecentContract.View>(view: V) :
    BasePresenter<V>(view),
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